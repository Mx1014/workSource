package com.everhomes.asset;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.order.PayProvider;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.pay.order.CreateOrderCommand;
import com.everhomes.pay.order.OrderCommandResponse;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.pay.order.SourceType;
import com.everhomes.paySDK.api.PayService;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.order.OrderPaymentStatus;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.order.PayServiceErrorCode;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.order.SrvOrderPaymentNotificationCommand;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Service
public class AssetPayServiceImplForEnt implements AssetPayServiceForEnt{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetPayServiceImplForEnt.class);
	
	@Autowired 
    private PayService payServiceV2;
	
	@Autowired 
	private UserProvider userProvider;
	
	@Autowired
    private PayProvider payProvider;
	
	@Autowired
    private ConfigurationProvider configurationProvider;
	
	@Value("${server.contextPath:}")
    private String contextPath;
	
    public PreOrderDTO createPreOrder(PreOrderCommand cmd) {
        PreOrderDTO preOrderDTO = null;
        //1、检查买方（付款方）是否有会员，无则创建
        User userById = userProvider.findUserById(UserContext.currentUserId());
        UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(userById.getId(), cmd.getNamespaceId());
        String userIdenify = null;
        if(userIdentifier != null) {
        	userIdenify = userIdentifier.getIdentifierToken();
        }
        PayUserDTO payUserDTO = checkAndCreatePaymentUser(cmd.getPayerId().toString(), "NS" + cmd.getNamespaceId().toString(), userIdenify, null, null, null);
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
        
        //4、组装支付方式
        //preOrderDTO = orderCommandResponseToDto(orderCommandResponse, cmd);
        preOrderDTO = ConvertHelper.convert(orderCommandResponse, PreOrderDTO.class);
        
        //5、保存订单信息
        saveOrderRecord(orderCommandResponse, cmd.getOrderId(), com.everhomes.pay.order.OrderType.PURCHACE.getCode());

        //6、返回
        return preOrderDTO;
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
        createOrderCmd.setSourceType(SourceType.PC.getCode());
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
    
}
