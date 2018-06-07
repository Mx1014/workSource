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
import com.everhomes.paySDK.api.PayService;
import com.everhomes.paySDK.pojo.PayUserDTO;
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
    private PayService payServiceV2;
	
	@Autowired
    private PayProvider payProvider;
	
	@Autowired
    private ConfigurationProvider configurationProvider;
	
	@Value("${server.contextPath:}")
    private String contextPath;
	
	/**
	 * 列出当前项目下所有的收款方账户
	 */
	public List<ListBizPayeeAccountDTO> listBizPayeeAccounts(Long orgnizationId, String... tags) {
		ListBusinessUsersCommand cmd = new ListBusinessUsersCommand();
        // 给支付系统的bizUserId的形式：EhOrganizations1037001
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
	
    public PreOrderDTO createPreOrder(PreOrderCommand cmd) {
        PreOrderDTO preOrderDTO = null;
        //1、检查买方（付款方）是否有会员，无则创建
        PayUserDTO payUserDTO = checkAndCreatePaymentUser(cmd.getPayerId(), cmd.getNamespaceId());
        if(payUserDTO != null) {
        	cmd.setPayerId(payUserDTO.getId());
        }else {
        	LOGGER.error("payerUserId no find, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
                    "payerUserId no find");
        }

        //2、收款方是否有会员，无则报错
        Long payeeUserId = cmd.getBizPayeeId();
        if(payeeUserId == null) {
        	LOGGER.error("payeeUserId no find, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
                    "payeeUserId no find");
        }
        
        //3、组装报文，发起下单请求
        OrderCommandResponse orderCommandResponse = createOrder(cmd);

        //4、保存订单信息
        saveOrderRecord(orderCommandResponse, cmd.getOrderId(), com.everhomes.pay.order.OrderType.PURCHACE.getCode());

        //5、返回
        return preOrderDTO;
    }
    
    private void saveOrderRecord(OrderCommandResponse orderCommandResponse, Long orderId, Integer paymentOrderType){
        PaymentOrderRecord record = ConvertHelper.convert(orderCommandResponse, PaymentOrderRecord.class);
        record.setOrderNum(orderCommandResponse.getBizOrderNum());
        record.setOrderId(orderId);
        //PaymentOrderId为支付系统传来的orderId
        record.setPaymentOrderId(orderCommandResponse.getOrderId());
        record.setPaymentOrderType(paymentOrderType);
        payProvider.createPaymentOrderRecord(record);
    }
    
    //检查买方（付款方）会员，无则创建
    private PayUserDTO checkAndCreatePaymentUser(Long payerId, Integer namespaceId){
    	PayUserDTO payUserDTO = new PayUserDTO();
        //根据支付帐号ID列表查询帐号信息
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("checkAndCreatePaymentUser request={}", payerId);
        }
        List<PayUserDTO> payUserDTOs = payServiceV2.listPayUsersByIds(Arrays.asList(payerId));
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("checkAndCreatePaymentUser response={}", payUserDTOs);
        }
        if(payUserDTOs == null || payUserDTOs.size() == 0){
            //创建个人账号
        	payUserDTO = payServiceV2.createPersonalPayUserIfAbsent(payerId.toString(), namespaceId.toString());
        }else {
        	payUserDTO = payUserDTOs.get(0);
        }
        return payUserDTO;
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
    
    private OrderCommandResponse createOrder(PreOrderCommand preOrderCommand){
        //组装参数
        CreateOrderCommand createOrderCommand = newCreateOrderCommandForPay(preOrderCommand);
        //向支付预下单
        CreateOrderRestResponse createOrderResp = createOrderPayV2(createOrderCommand);
        if(!createOrderResp.getErrorCode().equals(200)) {
            LOGGER.error("create order fail");
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
                    "create order fail");
        }
        OrderCommandResponse  response = createOrderResp.getResponse();
        return response;
    }
    
    private CreateOrderCommand newCreateOrderCommandForPay(PreOrderCommand cmd){
        //PaymentParamsDTO转为Map
        Map<String, String> flattenMap = new HashMap<>();
        StringHelper.toStringMap(null, cmd.getPaymentParams(), flattenMap);

        CreateOrderCommand createOrderCmd = new CreateOrderCommand();
        //业务系统中的订单号，请在整个业务系统中约定好唯一规则；
        String BizOrderNum  = getOrderNum(cmd.getOrderId(),cmd.getOrderType());
        LOGGER.info("BizOrderNum is = {} "+BizOrderNum);
        createOrderCmd.setBizOrderNum(BizOrderNum);
        createOrderCmd.setClientAppName(cmd.getClientAppName());
        createOrderCmd.setPayerUserId(cmd.getPayerId());//付款方ID
        createOrderCmd.setPayeeUserId(cmd.getBizPayeeId());//收款方ID
        createOrderCmd.setAmount(cmd.getAmount());//需要将元转化为分，使用此类: AmountUtil.unitToCent()
        createOrderCmd.setPaymentParams(flattenMap);
        createOrderCmd.setPaymentType(cmd.getPaymentType());
        if(cmd.getExpiration() != null) {
            createOrderCmd.setExpirationMillis(cmd.getExpiration());
        }
        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
        String backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"asset.pay.v2.callback.url", "");
        String backUrl = homeUrl + contextPath + backUri;
        createOrderCmd.setBackUrl(backUrl);
        createOrderCmd.setExtendInfo(cmd.getExtendInfo());
        createOrderCmd.setGoodsName("物业缴费");
        createOrderCmd.setGoodsDescription(null);
        createOrderCmd.setIndustryName(null);
        createOrderCmd.setIndustryCode(null);
        createOrderCmd.setSourceType(SourceType.MOBILE.getCode());
        createOrderCmd.setOrderRemark1(String.valueOf(cmd.getOrderType()));
	    createOrderCmd.setOrderRemark2(String.valueOf(cmd.getOrderId()));
	    createOrderCmd.setOrderRemark3(String.valueOf(cmd.getCommunityId()));
	    createOrderCmd.setOrderRemark4(null);
	    createOrderCmd.setOrderRemark5(null);
	    if(UserContext.getCurrentNamespaceId() != null) {
	    	createOrderCmd.setAccountCode(UserContext.getCurrentNamespaceId().toString());
	    }
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
        CreateOrderRestResponse response = payServiceV2.createPurchaseOrder(cmd);
        if(LOGGER.isDebugEnabled()) {LOGGER.debug("createOrderPayV2-response=" + GsonUtil.toJson(response));}
        return response;
    }

}
