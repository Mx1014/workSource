package com.everhomes.asset;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.everhome.paySDK.pojo.PayUserDTO;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.order.PayProvider;
import com.everhomes.order.PaymentAccount;
import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.order.PaymentServiceConfig;
import com.everhomes.order.PaymentServiceConfigHandler;
import com.everhomes.order.PaymentUser;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.pay.base.RestClient;
import com.everhomes.pay.order.CreateOrderCommand;
import com.everhomes.pay.order.OrderCommandResponse;
import com.everhomes.pay.order.PaymentType;
import com.everhomes.pay.order.SettlementType;
import com.everhomes.pay.order.SourceType;
import com.everhomes.pay.order.ValidationType;
import com.everhomes.pay.rest.ApiConstants;
import com.everhomes.pay.user.BindPhoneCommand;
import com.everhomes.pay.user.BusinessUserType;
import com.everhomes.pay.user.ListBusinessUsersCommand;
import com.everhomes.pay.user.RegisterBusinessUserCommand;
import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.rest.order.PayServiceErrorCode;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.rest.order.PaymentUserStatus;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.rest.pay.controller.RegisterBusinessUserRestResponse;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Service
public class AssetPayServiceImpl implements AssetPayService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetPayServiceImpl.class);
	
	private static final Long SYSTEMID = 5L;
	
	private RestClient restClient = null;
	
	@Autowired 
    private com.everhome.paySDK.api.PayService payServiceV2;
	
	@Autowired
    private PayProvider payProvider;
	
	@Autowired
    private ContentServerService contentServerService;
	
	@Autowired
    private ConfigurationProvider configurationProvider;
	
	@Value("${server.contextPath:}")
    private String contextPath;
	
	/**
	 * 列出当前项目下所有的收款方账户
	 */
	public List<ListBizPayeeAccountDTO> listBizPayeeAccounts(Long orgnizationId, String... tags) {
		ListBusinessUsersCommand cmd = new ListBusinessUsersCommand();
        // 给支付系统的bizUserId的形式：EhBizBusinesses1037001
        //String userPrefix = "EhBizBusinesses";
		String userPrefix = "EhOrganizations";
        cmd.setBizUserId(userPrefix + orgnizationId);
        if(tags != null && tags.length > 0) {
            cmd.setTag1s(Arrays.asList(tags));
        }
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("List biz payee accounts(request), orgnizationId={}, tags={}, cmd={}", orgnizationId, tags, cmd);
        }
        List<PayUserDTO> payUserDTOs = payServiceV2.getPayUserList(cmd.getBizUserId(), cmd.getTag1s());
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("List biz payee accounts(response), orgnizationId={}, tags={}, response={}", orgnizationId, tags, GsonUtil.toJson(payUserDTOs));
        }
        List<ListBizPayeeAccountDTO> result = new ArrayList<ListBizPayeeAccountDTO>();        
        if(payUserDTOs != null){
            for(PayUserDTO payUserDTO : payUserDTOs) {
                ListBizPayeeAccountDTO dto = new ListBizPayeeAccountDTO();
                // 支付系统中的用户ID
                dto.setAccountId(payUserDTO.getId());
                // 用户向支付系统注册帐号时填写的帐号名称
                dto.setAccountName(payUserDTO.getRemark());
                // 帐号类型，1-个人帐号、2-企业帐号
                Integer userType = payUserDTO.getUserType();
                if(userType != null && userType.equals(2)) {
                    dto.setAccountType(OwnerType.ORGANIZATION.getCode());
                } else {
                    dto.setAccountType(OwnerType.USER.getCode());
                }
                // 企业账户：0未审核 1审核通过  ; 个人帐户：0 未绑定手机 1 绑定手机
                Integer registerStatus = payUserDTO.getRegisterStatus();
                if(registerStatus != null && registerStatus.intValue() == 1) {
                    dto.setAccountStatus(PaymentUserStatus.ACTIVE.getCode());
                } else {
                    dto.setAccountStatus(PaymentUserStatus.WAITING_FOR_APPROVAL.getCode());
                }
                result.add(dto);
            }
        }
        return result;
	}
	
	/**
     * 1、检查是否已经下单
     * 2、检查买方是否有会员，无则创建
     * 3、收款方是否有会员，无则报错
     * 4、获取在支付系统中的账号，用户与支付系统交互
     * 5、组装报文，发起下单请求
     * 6、组装支付方式
     * 7、保存订单信息
     * 8、返回结果
     */
    public PreOrderDTO createPreOrder(PreOrderCommand cmd) {
        PreOrderDTO preOrderDTO = null;
        //1、查order表，如果order已经存在，则返回已有的合同，交易停止；否则，继续
        PaymentOrderRecord orderRecord = payProvider.findOrderRecordByOrder(cmd.getOrderType(), cmd.getOrderId());
        if (orderRecord != null) {
            preOrderDTO = recordToDto(orderRecord, cmd);
            return preOrderDTO;
        }

        //2、检查买方（付款方）是否有会员，无则创建
        PaymentUser paymentUser = checkAndCreatePaymentUser(cmd.getPayerId());

        //3、收款方是否有会员，无则报错
        PaymentServiceConfig serviceConfig = checkPaymentService(cmd.getNamespaceId(), cmd.getOrderType(), cmd.getResourceType(), cmd.getResourceId());

        //4、获取在支付系统中的账号，用户与支付系统交互,
        PaymentAccount paymentAccount = findPaymentAccount(SYSTEMID);
        if (paymentAccount == null) {
            LOGGER.error("payment account no find system_id={}", SYSTEMID);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_ACCOUNT_NO_FIND,
                    "payment account no find");
        }

        //5、组装报文，发起下单请求
        OrderCommandResponse orderCommandResponse = createOrder(cmd, paymentUser, serviceConfig, paymentAccount);

        //6、组装支付方式
        preOrderDTO = orderCommandResponseToDto(orderCommandResponse, cmd, serviceConfig);

        //7、保存订单信息
        saveOrderRecord(orderCommandResponse, cmd.getOrderId(), serviceConfig.getOrderType(),  serviceConfig.getId(), com.everhomes.pay.order.OrderType.PURCHACE.getCode());


        //8、返回
        return preOrderDTO;
    }
    
    private PreOrderDTO orderCommandResponseToDto(OrderCommandResponse orderCommandResponse, PreOrderCommand cmd, PaymentServiceConfig service){
        PreOrderDTO dto = ConvertHelper.convert(orderCommandResponse, PreOrderDTO.class);
        dto.setAmount(cmd.getAmount());
        List<PayMethodDTO> payMethods = listPayMethods(cmd.getNamespaceId(), cmd.getPaymentType(), cmd.getPaymentParams(), service);
        dto.setPayMethod(payMethods);
        Long expiredIntervalTime = getExpiredIntervalTime(cmd.getExpiration());
        dto.setExpiredIntervalTime(expiredIntervalTime);
        dto.setOrderId(cmd.getOrderId());
        return dto;
    }
    
    private void saveOrderRecord(OrderCommandResponse orderCommandResponse, Long orderId, String orderType, Long serviceConfigId, Integer paymentOrderType){

        PaymentOrderRecord record = ConvertHelper.convert(orderCommandResponse, PaymentOrderRecord.class);

        record.setOrderNum(orderCommandResponse.getBizOrderNum());

        record.setOrderId(orderId);
        //PaymentOrderId为支付系统传来的orderId
        record.setPaymentOrderId(orderCommandResponse.getOrderId());
        record.setServiceConfigId(serviceConfigId);
        record.setOrderType(orderType);
        record.setPaymentOrderType(paymentOrderType);
        payProvider.createPaymentOrderRecord(record);
    }
    
    private PreOrderDTO recordToDto(PaymentOrderRecord record, PreOrderCommand cmd){
        PreOrderDTO dto = ConvertHelper.convert(record, PreOrderDTO.class);

        PaymentServiceConfig service = checkPaymentService(cmd.getNamespaceId(), cmd.getOrderType(), cmd.getResourceType(), cmd.getResourceId());
        dto.setAmount(cmd.getAmount());
        dto.setExtendInfo(cmd.getExtendInfo());

        List<PayMethodDTO> payMethods = listPayMethods(cmd.getNamespaceId(), cmd.getPaymentType(), cmd.getPaymentParams(), service);
        dto.setPayMethod(payMethods);
        Long expiredIntervalTime = getExpiredIntervalTime(cmd.getExpiration());
        dto.setExpiredIntervalTime(expiredIntervalTime);
        return dto;
    }
    
    /**
     * 检查卖方信息，无则报错
     */
    private PaymentServiceConfig checkPaymentService(Integer namespaceId, String orderType, String resourceType, Long resourceId){

        PaymentServiceConfigHandler handler = getServiceConfigHandler(orderType);

        PaymentServiceConfig paymentServiceConfig = handler.findPaymentServiceConfig(namespaceId, orderType, resourceType, resourceId);

        if(paymentServiceConfig == null){
            LOGGER.error("payment service config no find, namespaceId={}, orderType={}, resourceType={}, resourceId={}"
                    , namespaceId, orderType, resourceType, resourceId);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
                    "payment service config no find");
        }

        return paymentServiceConfig;
    }
    
    private List<PayMethodDTO> listPayMethods(Integer namespaceId, Integer paymentType, PaymentParamsDTO paramsDTO, PaymentServiceConfig service){

        List<PayMethodDTO> payMethods = payProvider.listPayMethods(namespaceId, paymentType, service.getOrderType(),
                service.getOwnerType(), service.getOwnerId(), service.getResourceType(), service.getResourceId());

        if(payMethods != null && payMethods.size() > 0){
            for(PayMethodDTO r : payMethods) {
//                PaymentExtendInfo extendInfo = new PaymentExtendInfo();
//                extendInfo.setGetOrderInfoUrl(getPayMethodExtendInfo());
                r.setExtendInfo(getPayMethodExtendInfo());

                if(r.getPaymentParams() == null){
                    r.setPaymentParams(new PaymentParamsDTO());
                }
                //微信公众号支付时，acct原样返回
                if(PaymentType.fromCode(paymentType) == PaymentType.WECHAT_JS_PAY &&
                        PaymentType.fromCode(r.getPaymentType()) == PaymentType.WECHAT_JS_PAY &&
                        paramsDTO != null){
                    r.getPaymentParams().setAcct(paramsDTO.getAcct());
                }
                //转化为可以访问的url
                String url = contentServerService.parserUri(r.getPaymentLogo());
                r.setPaymentLogo(url);
            }
        }

        return payMethods;
    }
    
    private Long getExpiredIntervalTime(Long expiration){
        Long expiredIntervalTime = null;
        if(expiration != null){
            expiredIntervalTime = expiration - System.currentTimeMillis();
            //转换成秒
            expiredIntervalTime = expiredIntervalTime/1000;
            if(expiredIntervalTime < 0){
               expiredIntervalTime = 0L;
            }
        }
        return expiredIntervalTime;
    }
    
    private PaymentServiceConfigHandler getServiceConfigHandler(String orderType) {
        PaymentServiceConfigHandler handler = null;
        String handlerName = PaymentServiceConfigHandler.PAYMENT_SERVICE_CONFIG_HANDLER_PREFIX +this.getOrderTypeCode(orderType);
        
        // 收款方不一定实现handler，此时会找不到handler而抛异常，为了减少日志这些不必要的堆栈打印，故只打一行warning by lqs 20170930
        try {
            handler = PlatformContext.getComponent(handlerName);
        } catch (NoSuchBeanDefinitionException e) {
            LOGGER.warn("Failed to find handler, orderType={}, handlerName={}", orderType, handlerName);
        }

        if(handler == null){
            handler = PlatformContext.getComponent(PaymentServiceConfigHandler.PAYMENT_SERVICE_CONFIG_HANDLER_PREFIX +"DEFAULT");
        }

        return handler;
    }

    private String getOrderTypeCode(String orderType) {
        Integer code = OrderType.OrderTypeEnum.getCodeByPyCode(orderType);
        if(code==null){
            LOGGER.error("Invalid parameter,orderType not found in OrderType.orderType="+orderType);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter,orderType not found in OrderType");
        }
        LOGGER.debug("orderTypeCode="+code);
        return String.valueOf(code);
    }
    
    private String getPayMethodExtendInfo(){
        String payV2HomeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.home.url", "");
        String getOrderInfoUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.orderPaymentStatusQueryUri", "");

        String format = "{\"getOrderInfoUrl\":\"%s\"}";
        return String.format(format, payV2HomeUrl+getOrderInfoUri);
    }
    
    //检查买方（付款方）会员，无则创建
    private PaymentUser checkAndCreatePaymentUser(Long payerId){
        PaymentUser paymentUser = new PaymentUser();
        //根据支付帐号ID列表查询帐号信息
        //List<PayUserDTO> payUserDTOs = payServiceV2.listPayUsersByIds(arg0)
        if(paymentUser == null){
            //创建
        }
        return paymentUser;
    }
    
    public PaymentUser createPaymentUser(int businessUserType, String ownerType, Long ownerId){

        Long id = payProvider.getNewPaymentUserId();

        RegisterBusinessUserRestResponse restResponse = registerPayV2User(businessUserType,  ownerType+String.valueOf(ownerId));

        if(restResponse == null || restResponse.getErrorCode() == null || restResponse.getErrorCode() != 200 ){
            LOGGER.error("register user fail, businessUserType={}, ownerType={}, ownerId={}", businessUserType, ownerType, ownerId);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_REGISTER_USER_FAIL,
                    "register user fail");
        }

        Long paymentUserId = restResponse.getResponse().getId();
        String defaultPhone = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"default.bind.phone", "");
        StringRestResponse bindResponse = bindPhonePayV2(paymentUserId, defaultPhone);
        String errorDescription = bindResponse.getErrorDescription();
        if(errorDescription!=null && errorDescription.indexOf("30024")==-1){
            if(bindResponse == null || bindResponse.getErrorCode() == null || bindResponse.getErrorCode() != 200 ){
                LOGGER.error("bind phone fail, businessUserType={}, ownerType={}, ownerId={}, phone={}", businessUserType, ownerType, ownerId, defaultPhone);
                throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_BIND_PHONE_FAIL,
                        "bind phone fail");
            }
        }

        PaymentUser paymentUser = new PaymentUser();
        paymentUser.setId(id);
        paymentUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
        paymentUser.setOwnerType(ownerType);
        paymentUser.setOwnerId(ownerId);
        paymentUser.setPaymentUserType(businessUserType);
        paymentUser.setPaymentUserId(paymentUserId);
        // 买方会员默认就是正常状态，不需要审核 by lqs 20171124
        paymentUser.setStatus(PaymentUserStatus.ACTIVE.getCode());
        payProvider.createPaymentUser(paymentUser);
        return paymentUser;
    }
    
    /**
     * 去支付系统创建用户
     */
    private RegisterBusinessUserRestResponse registerPayV2User(Integer businessUserType, String bizUserId){

        RegisterBusinessUserCommand cmd = new RegisterBusinessUserCommand();
        cmd.setBizSystemId(SYSTEMID);
        cmd.setUserType(businessUserType);
        cmd.setBizUserId(bizUserId);

        if(LOGGER.isDebugEnabled()) {LOGGER.debug("registerPayV2User-command=" + GsonUtil.toJson(cmd));}
        RegisterBusinessUserRestResponse response = restClient.restCall(
                "POST",
                ApiConstants.MEMBER_REGISTERBUSINESSUSER_URL,
                cmd,
                RegisterBusinessUserRestResponse.class);
        if(LOGGER.isDebugEnabled()) {LOGGER.debug("createOrderPayV2-response=" + GsonUtil.toJson(response));}
        return response;
    }
    
    /**
     * 去支付系统创建用户
     */
    private StringRestResponse bindPhonePayV2(Long paymentUserId, String phone){
        BindPhoneCommand cmd = new BindPhoneCommand();
        cmd.setPhone(phone);
        cmd.setUserId(paymentUserId);

        if(LOGGER.isDebugEnabled()) {LOGGER.debug("bindPhonePayV2-command=" + GsonUtil.toJson(cmd));}
        StringRestResponse response = restClient.restCall(
                "POST",
                ApiConstants.MEMBER_BINDPHONE_URL,
                cmd,
                StringRestResponse.class);
        if(LOGGER.isDebugEnabled()) {LOGGER.debug("bindPhonePayV2-response=" + GsonUtil.toJson(response));}

        return response;

    }
    
    //获取
    private PaymentAccount findPaymentAccount(Long systemId){
        return payProvider.findPaymentAccountBySystemId(systemId);
    }
    
    private OrderCommandResponse createOrder(PreOrderCommand preOrderCommand, PaymentUser paymentUser, PaymentServiceConfig serviceConfig, PaymentAccount paymentAccount){
        //组装参数
        CreateOrderCommand createOrderCommand = newCreateOrderCommandForPay(preOrderCommand, paymentUser, serviceConfig, paymentAccount);

        CreateOrderRestResponse createOrderResp = createOrderPayV2(createOrderCommand);

        if(!createOrderResp.getErrorCode().equals(200)) {
            LOGGER.error("create order fail");
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
                    "create order fail");
        }

        OrderCommandResponse  response = createOrderResp.getResponse();

        return response;
    }
    
    private CreateOrderCommand newCreateOrderCommandForPay(PreOrderCommand cmd, PaymentUser paymentUser, PaymentServiceConfig serviceConfig, PaymentAccount paymentAccount){

        //PaymentParamsDTO转为Map
        Map<String, String> flattenMap = new HashMap<>();
        StringHelper.toStringMap(null, cmd.getPaymentParams(), flattenMap);

        CreateOrderCommand createOrderCmd = new CreateOrderCommand();
        createOrderCmd.setOrderType(com.everhomes.pay.order.OrderType.PURCHACE.getCode());
        createOrderCmd.setAmount(cmd.getAmount());//需要将元转化为分，使用此类: AmountUtil.unitToCent()
        if(paymentAccount.getSystemId() != null) {
        	createOrderCmd.setBizSystemId(paymentAccount.getSystemId().longValue());
        }
        createOrderCmd.setClientAppName(cmd.getClientAppName());
        createOrderCmd.setPaymentType(cmd.getPaymentType());
        createOrderCmd.setValidationType(ValidationType.NO_VERIFY.getCode());
        createOrderCmd.setPaymentParams(flattenMap);
        
        // 在eh_payment_users中加上结算类型，结算类型以收款方为准，收款方没有配置时则使用默认值 by lqs 20171124
        //createOrderCmd.setSettlementType(null);
        PaymentUser payeetUser = payProvider.findPaymentUserByOwner(serviceConfig.getOwnerType(), serviceConfig.getOwnerId());
        SettlementType settlementType = null;
        if(payeetUser != null) {
            settlementType = SettlementType.fromCode(payeetUser.getSettlementType());
        }
        if(settlementType == null) {
            settlementType = SettlementType.WEEKLY;
        }
        createOrderCmd.setSettlementType(settlementType.getCode());
        
        createOrderCmd.setSplitRuleId(serviceConfig.getPaymentSplitRuleId());
        if(cmd.getExpiration() != null) {
            createOrderCmd.setExpirationMillis(cmd.getExpiration());
        }
        createOrderCmd.setPayeeUserId(serviceConfig.getPaymentUserId());
        createOrderCmd.setPayerUserId(paymentUser.getPaymentUserId());
        createOrderCmd.setFrontUrl(null);

        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
        String backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.callback.url", "");
        String backUrl = homeUrl + contextPath + backUri;

        createOrderCmd.setBackUrl(backUrl);
        createOrderCmd.setExtendInfo(cmd.getExtendInfo());
        createOrderCmd.setGoodsName(serviceConfig.getName());
        createOrderCmd.setGoodsDescription(null);
        createOrderCmd.setIndustryCode(null);
        createOrderCmd.setIndustryName(null);
        createOrderCmd.setSourceType(SourceType.MOBILE.getCode());

//        //BizOrderNum需要传PaymentOrderRecords表记录的id，此处先申请id，在返回值中使用BizOrderNum做为record的id
//        Long orderRecordId = payProvider.getNewPaymentOrderRecordId();


        String BizOrderNum  = getOrderNum(cmd.getOrderId(),cmd.getOrderType());
        LOGGER.info("BizOrderNum is = {} "+BizOrderNum);
        createOrderCmd.setBizOrderNum(BizOrderNum);

        createOrderCmd.setOrderRemark1(String.valueOf(cmd.getOrderType()));
//        createOrderCmd.setOrderRemark1(String.valueOf(serviceConfig.getId()));
        createOrderCmd.setOrderRemark2(String.valueOf(cmd.getOrderId()));
        createOrderCmd.setOrderRemark3(String.valueOf(cmd.getCommunityId()));
        createOrderCmd.setOrderRemark4(null);
        createOrderCmd.setOrderRemark5(null);
        createOrderCmd.setCommitFlag(0);
        if(cmd.getCommitFlag() != null){
            createOrderCmd.setCommitFlag(cmd.getCommitFlag());
        }
        //为微信公众号新增commitFlag

        return createOrderCmd;
    }
    
    private String getOrderNum(Long orderId, String orderType) {
        String v2code = OrderType.OrderTypeEnum.getV2codeByPyCode(orderType);
        DecimalFormat df = new DecimalFormat("00000000000000000");
        String orderIdStr = df.format(orderId);
        if(orderIdStr!=null && orderIdStr.length()>17){
            orderIdStr = orderIdStr.substring(2);
        }
        return v2code+orderIdStr;
    }

    private CreateOrderRestResponse createOrderPayV2(CreateOrderCommand cmd){
        if(LOGGER.isDebugEnabled()) {LOGGER.debug("createOrderPayV2-command=" + GsonUtil.toJson(cmd));}
        CreateOrderRestResponse response = restClient.restCall(
                "POST",
                ApiConstants.ORDER_CREATEORDER_URL,
                cmd,
                CreateOrderRestResponse.class);
        if(LOGGER.isDebugEnabled()) {LOGGER.debug("createOrderPayV2-response=" + GsonUtil.toJson(response));}
        return response;
    }

}
