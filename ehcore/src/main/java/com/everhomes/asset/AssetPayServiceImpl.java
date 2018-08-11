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
import com.everhomes.gorder.sdk.order.OrderService;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.order.PayProvider;
import com.everhomes.order.PaymentAccount;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.order.PaymentServiceConfig;
import com.everhomes.order.PaymentServiceConfigHandler;
import com.everhomes.order.PaymentUser;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.pay.base.RestClient;
import com.everhomes.pay.order.CreateOrderCommand;
import com.everhomes.pay.order.OrderCommandResponse;
import com.everhomes.pay.order.OrderDTO;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.pay.order.PaymentType;
import com.everhomes.pay.order.SettlementType;
import com.everhomes.pay.order.SourceType;
import com.everhomes.pay.order.ValidationType;
import com.everhomes.pay.rest.ApiConstants;
import com.everhomes.pay.user.BindPhoneCommand;
import com.everhomes.pay.user.BusinessUserType;
import com.everhomes.pay.user.ListBusinessUsersCommand;
import com.everhomes.pay.user.RegisterBusinessUserCommand;
import com.everhomes.paySDK.PayUtil;
import com.everhomes.paySDK.api.PayService;
import com.everhomes.paySDK.pojo.PayOrderDTO;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.asset.AssetServiceErrorCode;
import com.everhomes.rest.gorder.controller.CreatePurchaseOrderRestResponse;
import com.everhomes.rest.gorder.order.CreatePurchaseOrderCommand;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.OrderPaymentStatus;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.rest.order.PayServiceErrorCode;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.rest.order.PaymentUserStatus;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.order.SrvOrderPaymentNotificationCommand;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.rest.pay.controller.RegisterBusinessUserRestResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;

@Service
public class AssetPayServiceImpl implements AssetPayService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetPayServiceImpl.class);
	
	@Autowired 
    private PayService payServiceV2;
	
	@Autowired 
	private UserProvider userProvider;
	
	@Autowired
	private ContentServerService contentServerService;
	
	@Autowired
    private PayProvider payProvider;
	
	@Autowired
    private ConfigurationProvider configurationProvider;
	
	@Value("${server.contextPath:}")
    private String contextPath;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private LocaleStringProvider localeStringProvider;
	
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
                dto.setAccountAliasName(payUserDTO.getUserAliasName());//企业名称（认证企业）
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
	
//    public PreOrderDTO createPreOrder(PreOrderCommand cmd) {
//        PreOrderDTO preOrderDTO = null;
//        //1、检查买方（付款方）是否有会员，无则创建
//        User userById = userProvider.findUserById(UserContext.currentUserId());
//        UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(userById.getId(), cmd.getNamespaceId());
//        String userIdenify = null;
//        if(userIdentifier != null) {
//        	userIdenify = userIdentifier.getIdentifierToken();
//        }
//        PayUserDTO payUserDTO = checkAndCreatePaymentUser(cmd.getPayerId().toString(), "NS" + cmd.getNamespaceId().toString(), userIdenify, null, null, null);
//        if(payUserDTO != null) {
//        	cmd.setPayerId(payUserDTO.getId());
//        }else {
//        	LOGGER.error("payerUserId no find, cmd={}", cmd);
//            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
//                    "payerUserId no find");
//        }
//
//        //2、收款方是否有会员，无则报错
//        Long payeeUserId = cmd.getBizPayeeId();
//        if(payeeUserId == null) {
//        	LOGGER.error("payeeUserId no find, cmd={}", cmd);
//            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
//                    "payeeUserId no find");
//        }
//        
//        //3、组装报文，发起下单请求
//        OrderCommandResponse orderCommandResponse = createOrder(cmd);
//        
//        //4、组装支付方式
//        preOrderDTO = orderCommandResponseToDto(orderCommandResponse, cmd);
//        
//        
//		//preOrderDTO.setPayMethod(orderCommandResponse.getPaymentMethods());//todo
//		//preOrderDTO.setPayMethod(getPayMethods(orderCommandResponse.getOrderPaymentStatusQueryUrl()));//todo
//
//        //5、保存订单信息
//        saveOrderRecord(orderCommandResponse, cmd.getOrderId(), com.everhomes.pay.order.OrderType.PURCHACE.getCode());
//
//        //6、返回
//        return preOrderDTO;
//    }
    public PreOrderDTO createPreOrder(PreOrderCommand cmd) {
        PreOrderDTO preOrderDTO = null;
        //1、检查买方（付款方）是否有会员，无则创建
        User buyer = userProvider.findUserById(UserContext.currentUserId());

        //2、收款方是否有会员，无则报错
        Long payeeUserId = cmd.getBizPayeeId();
        if(payeeUserId == null) {
            LOGGER.error("Payee user id not found(id in payment system), cmd={}", cmd);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
                    "Payee user id not found");
        }
        
        CreatePurchaseOrderCommand createOrderCommand = preparePurchaseOrderByBuyer(cmd, buyer);
        CreatePurchaseOrderRestResponse createOrderResp = orderService.createPurchaseOrder(createOrderCommand);
        if(!createOrderResp.getErrorCode().equals(200)) {
            LOGGER.error("create order fail");
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
                    "create order fail");
        }
        OrderCommandResponse response = createOrderResp.getResponse();

        //5、保存订单信息
        saveOrderRecord(orderCommandResponse, cmd.getOrderId(), com.everhomes.pay.order.OrderType.PURCHACE.getCode());

        //6、返回
        return preOrderDTO;
    }
    
    private CreatePurchaseOrderCommand preparePurchaseOrderByBuyer(PreOrderCommand cmd, User buyer){
        Map<String, String> flattenMap = new HashMap<>();
        StringHelper.toStringMap(null, cmd.getPaymentParams(), flattenMap);

        CreatePurchaseOrderCommand createOrderCmd = new CreatePurchaseOrderCommand();
        
        String accountCode = generateAccountCode(cmd.getNamespaceId());
        createOrderCmd.setAccountCode(accountCode);
        createOrderCmd.setClientAppName(cmd.getClientAppName());

        String BizOrderNum  = getOrderNum(cmd.getOrderId(),cmd.getOrderType());
        createOrderCmd.setBizOrderNum(BizOrderNum);
        
        createOrderCmd.setBuyerType(OwnerType.USER.getCode());
        createOrderCmd.setBuyerId(String.valueOf(buyer.getId()));
        UserIdentifier buyerIdentifier = userProvider.findUserIdentifiersOfUser(buyer.getId(), cmd.getNamespaceId());
        if(buyerIdentifier != null) {
            String buyerPhone = buyerIdentifier.getIdentifierToken();
            createOrderCmd.setBuyerPhone(buyerPhone);
        }
        
        createOrderCmd.setPayeeUserId(cmd.getBizPayeeId());//收款方ID
        createOrderCmd.setAmount(cmd.getAmount());
        createOrderCmd.setPaymentParams(flattenMap);
        createOrderCmd.setPaymentType(cmd.getPaymentType());
        if(cmd.getExpiration() != null) {
            createOrderCmd.setExpirationMillis(cmd.getExpiration());
        }
        
        createOrderCmd.setBackUrl(getPayCallbackUrl(cmd));
        createOrderCmd.setExtendInfo(cmd.getExtendInfo());
        //localeStringProvider.find(AssetServiceErrorCode.SCOPE, AssetServiceErrorCode.PAYMENT_GOOD_NAME, buyer.getLocale());
        createOrderCmd.setGoodsName("物业缴费");
        createOrderCmd.setGoodsDescription(null);
        createOrderCmd.setIndustryName(null);
        createOrderCmd.setIndustryCode(null);
        createOrderCmd.setSourceType(SourceType.MOBILE.getCode());
        createOrderCmd.setOrderRemark1("物业缴费");
        createOrderCmd.setOrderRemark2(String.valueOf(cmd.getOrderId()));
        createOrderCmd.setOrderRemark3(String.valueOf(cmd.getCommunityId()));
        createOrderCmd.setOrderRemark4(null);
        createOrderCmd.setOrderRemark5(null);
        
        return createOrderCmd;
    }
    
    private String getPayCallbackUrl(PreOrderCommand cmd) {
        String configKey = "pay.v2.callback.url.asset";
        String backUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), configKey, "");
        if(backUrl == null || backUrl.trim().length() == 0) {
            LOGGER.error("Payment callback url empty, configKey={}, cmd={}", configKey, cmd);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAY_CALLBACK_URL_EMPTY,
                    "Payment callback url empty");
        }
        
        if(!backUrl.toLowerCase().startsWith("http")) {
            String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
            backUrl = homeUrl + contextPath + backUrl;
        }
        
        return backUrl;
    }
    
    private String generateAccountCode(Integer namespaceId) {
        StringBuilder strBuilder = new StringBuilder();
        
        strBuilder.append("NS");
        strBuilder.append(namespaceId);
        
        return strBuilder.toString();
    }
    
    public void payNotify(OrderPaymentNotificationCommand cmd, PaymentCallBackHandler handler) {
    	if(LOGGER.isDebugEnabled()) {
    		LOGGER.debug("payNotify-command=" + GsonUtil.toJson(cmd));
    	}
    	//if(cmd == null || cmd.getPaymentErrorCode() != "200") {
    	if(cmd == null) {
    		LOGGER.error("payNotify fail, cmd={}", cmd);
    	}
    	//检查订单是否存在
        PaymentOrderRecord orderRecord = payProvider.findOrderRecordByOrderNum(cmd.getBizOrderNum());
        if(orderRecord == null){
            LOGGER.error("can not find order record by BizOrderNum={}", cmd.getBizOrderNum());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "can not find order record");
        }
        //此处将orderId设置成业务系统的orderid，方便业务调用。原orderId为支付系统的orderid，业务不需要知道。
        cmd.setOrderId(orderRecord.getOrderId());
    	SrvOrderPaymentNotificationCommand srvCmd = ConvertHelper.convert(cmd, SrvOrderPaymentNotificationCommand.class);
        com.everhomes.pay.order.OrderType orderType = com.everhomes.pay.order.OrderType.fromCode(cmd.getOrderType());
        if(orderType != null) {
            switch (orderType) {
                case PURCHACE:
                    if(cmd.getPaymentStatus()== OrderPaymentStatus.SUCCESS.getCode()){
                        //支付成功
                        handler.paySuccess(srvCmd);
                    }
//                    if(cmd.getPaymentStatus()==OrderPaymentStatus.FAILED.getCode()){
//                        //支付失败
//                        handler.payFail(srvCmd);
//                    }
                    break;
//                case REFUND:
//                    if(cmd.getPaymentStatus()== OrderPaymentStatus.SUCCESS.getCode()){
//                        //退款成功
//                        handler.refundSuccess(srvCmd);
//                    }
//                    if(cmd.getPaymentStatus()==OrderPaymentStatus.FAILED.getCode()){
//                        //退款失败
//                        handler.refundFail(srvCmd);
//                    }
//                    break;
                default:
                    LOGGER.error("unsupport orderType, orderType={}, cmd={}", orderType.getCode(), StringHelper.toJsonString(cmd));
            }
        }else {
            LOGGER.error("orderType is null, cmd={}", StringHelper.toJsonString(cmd));
        }
    }
    
    private void saveOrderRecord(OrderCommandResponse orderCommandResponse, Long orderId, Integer paymentOrderType){
        PaymentOrderRecord record = ConvertHelper.convert(orderCommandResponse, PaymentOrderRecord.class);
        record.setOrderNum(orderCommandResponse.getBizOrderNum());
        record.setOrderId(orderId);
        //PaymentOrderId为支付系统传来的orderId
        record.setPaymentOrderId(orderCommandResponse.getOrderId());
        record.setPaymentOrderType(paymentOrderType);
        record.setServiceConfigId(-1L);//暂时置为-1
        record.setOrderType("wuyeCode");
        payProvider.createPaymentOrderRecord(record);
    }
    
    //检查买方（付款方）会员，无则创建
    private PayUserDTO checkAndCreatePaymentUser(String userId, String accountCode,String userIdenify, String tag1, String tag2, String tag3){
    	PayUserDTO payUserDTO = new PayUserDTO();
    	String payerid = OwnerType.USER.getCode()+userId;
        //根据tag查询支付帐号
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("getPayUserList payerid={}", payerid);
        }
        List<PayUserDTO> payUserDTOs = payServiceV2.getPayUserList(payerid);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("getPayUserList response={}", payUserDTOs);
        }
        if(payUserDTOs == null || payUserDTOs.size() == 0){
            //创建个人账号
        	LOGGER.info("createPersonalPayUserIfAbsent payerid = {}, accountCode = {}, userIdenify={}",payerid,accountCode,userIdenify);
        	payUserDTO = payServiceV2.createPersonalPayUserIfAbsent(payerid, accountCode);
        	if(payUserDTO==null){
    			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_CREATE_USER_ACCOUNT,
    					"创建个人付款账户失败");
    		}
        	String s = payServiceV2.bandPhone(payUserDTO.getId(), userIdenify);//绑定手机号
        }else {
        	payUserDTO = payUserDTOs.get(0);
        }
        return payUserDTO;
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
        OrderCommandResponse response = createOrderResp.getResponse();
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
        createOrderCmd.setAmount(cmd.getAmount());
        createOrderCmd.setPaymentParams(flattenMap);
        createOrderCmd.setPaymentType(cmd.getPaymentType());
        if(cmd.getExpiration() != null) {
            createOrderCmd.setExpirationMillis(cmd.getExpiration());
        }
        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
        String backUri;
        if(UserContext.getCurrentNamespaceId().equals(999993)) {
        	//由于海岸馨服务是定制的
        	backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.callback.url.pmsy", "");
        }else {
        	backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.callback.url.asset", "");
        }
        String backUrl = homeUrl + contextPath + backUri;
        createOrderCmd.setBackUrl(backUrl);
        createOrderCmd.setExtendInfo(cmd.getExtendInfo());
        createOrderCmd.setGoodsName("物业缴费");
        createOrderCmd.setGoodsDescription(null);
        createOrderCmd.setIndustryName(null);
        createOrderCmd.setIndustryCode(null);
        createOrderCmd.setSourceType(SourceType.MOBILE.getCode());
        //createOrderCmd.setOrderRemark1(String.valueOf(cmd.getOrderType()));
        createOrderCmd.setOrderRemark1("物业缴费");
	    createOrderCmd.setOrderRemark2(String.valueOf(cmd.getOrderId()));
	    createOrderCmd.setOrderRemark3(String.valueOf(cmd.getCommunityId()));
	    createOrderCmd.setOrderRemark4(null);
	    createOrderCmd.setOrderRemark5(null);
	    if(UserContext.getCurrentNamespaceId() != null) {
	    	createOrderCmd.setAccountCode("NS" + UserContext.getCurrentNamespaceId().toString());
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
    
    private PreOrderDTO orderCommandResponseToDto(OrderCommandResponse orderCommandResponse, PreOrderCommand cmd){
        PreOrderDTO dto = ConvertHelper.convert(orderCommandResponse, PreOrderDTO.class);
        List<PayMethodDTO> payMethods = new ArrayList<>();//业务系统自己的支付方式格式
        List<com.everhomes.pay.order.PayMethodDTO> bizPayMethods = orderCommandResponse.getPaymentMethods();//支付系统传回来的支付方式
        String format = "{\"getOrderInfoUrl\":\"%s\"}";
        for(com.everhomes.pay.order.PayMethodDTO bizPayMethod : bizPayMethods) {
        	PayMethodDTO payMethodDTO = new PayMethodDTO();//支付方式
        	payMethodDTO.setPaymentName(bizPayMethod.getPaymentName());
        	payMethodDTO.setExtendInfo(String.format(format, orderCommandResponse.getOrderPaymentStatusQueryUrl()));
        	String paymentLogo = contentServerService.parserUri(bizPayMethod.getPaymentLogo());
        	payMethodDTO.setPaymentLogo(paymentLogo);
        	payMethodDTO.setPaymentType(bizPayMethod.getPaymentType());
        	PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
        	com.everhomes.pay.order.PaymentParamsDTO bizPaymentParamsDTO = bizPayMethod.getPaymentParams();
        	if(bizPaymentParamsDTO != null) {
        		paymentParamsDTO.setPayType(bizPaymentParamsDTO.getPayType());
        	}
        	payMethodDTO.setPaymentParams(paymentParamsDTO);
        	payMethods.add(payMethodDTO);
        }
        dto.setPayMethod(payMethods);
        dto.setExpiredIntervalTime(orderCommandResponse.getExpirationMillis());
        dto.setAmount(cmd.getAmount());
        dto.setOrderId(cmd.getOrderId());
        return dto;
    }
    /*public List<PayMethodDTO> getPayMethods(String paymentStatusQueryUrl) {
		List<PayMethodDTO> payMethods = new ArrayList<>();
		String format = "{\"getOrderInfoUrl\":\"%s\"}";
		PayMethodDTO alipay = new PayMethodDTO();
		alipay.setPaymentName("支付宝支付");
		PaymentParamsDTO alipayParamsDTO = new PaymentParamsDTO();
		alipayParamsDTO.setPayType("A01");
		alipay.setExtendInfo(String.format(format, paymentStatusQueryUrl));
		String url = contentServerService.parserUri("cs://1/image/aW1hZ2UvTVRveVpEWTNPV0kwWlRJMU0yRTFNakJtWkRCalpETTVaalUzTkdaaFltRmtOZw");
		alipay.setPaymentLogo(url);
		alipay.setPaymentParams(alipayParamsDTO);
		alipay.setPaymentType(8);
		payMethods.add(alipay);

		PayMethodDTO wxpay = new PayMethodDTO();
		wxpay.setPaymentName("微信支付");
		wxpay.setExtendInfo(String.format(format, paymentStatusQueryUrl));
		url = contentServerService.parserUri("cs://1/image/aW1hZ2UvTVRveU1UUmtaRFExTTJSbFpETXpORE5rTjJNME9Ua3dOVFkxTVRNek1HWXpOZw");
		wxpay.setPaymentLogo(url);
		PaymentParamsDTO wxParamsDTO = new PaymentParamsDTO();
		wxParamsDTO.setPayType("no_credit");
		wxpay.setPaymentParams(wxParamsDTO);
		wxpay.setPaymentType(1);

		payMethods.add(wxpay);
		return payMethods;
	}*/

}
