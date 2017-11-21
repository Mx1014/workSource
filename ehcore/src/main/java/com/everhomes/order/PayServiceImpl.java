//@formatter:off
package com.everhomes.order;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.pay.base.RestClient;
import com.everhomes.pay.order.*;
import com.everhomes.pay.rest.ApiConstants;
import com.everhomes.pay.user.BindPhoneCommand;
import com.everhomes.pay.user.BusinessUserType;
import com.everhomes.pay.user.RegisterBusinessUserCommand;
import com.everhomes.query.QueryBuilder;
import com.everhomes.query.QueryCondition;
import com.everhomes.rest.MapListRestResponse;
import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.group.GroupServiceErrorCode;
import com.everhomes.rest.order.*;
import com.everhomes.rest.order.OrderPaymentStatus;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.rest.pay.controller.QueryBalanceRestResponse;
import com.everhomes.rest.pay.controller.RegisterBusinessUserRestResponse;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class PayServiceImpl implements PayService, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private static final Integer SYSTEMID = 1;

    @Value("${server.contextPath:}")
    private String contextPath;

    @Autowired
    private PayProvider payProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private ContentServerService contentServerService;

    private RestClient restClient = null;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String payHomeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.home.url", "");
        PaymentAccount paymentAccount = findPaymentAccount(SYSTEMID);
        if(paymentAccount == null){
            LOGGER.error("payment account no find system_id={}", SYSTEMID);
            return;
        }
        restClient = new RestClient(payHomeUrl, paymentAccount.getAppKey(), paymentAccount.getSecretKey());
    }

    /**
     *
     * @param namespaceId 域空间
     * @param clientAppName 客户端realm值
     * @param orderType 订单类型 参考 com.everhomes.rest.order.OrderType
     * @param orderId 订单Id
     * @param payerId 卖家用户ID
     * @param amount 支付金额，BigDecimal转换本类提供了方法changePayAmount
     * @return
     */
    @Override
    public PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount) {
        return  createAppPreOrder(namespaceId, clientAppName, orderType, orderId, payerId, amount , null, null, null);
    }

    /**
     *
     * @param namespaceId 域空间
     * @param clientAppName 客户端realm值
     * @param orderType 订单类型 参考 com.everhomes.rest.order.OrderType
     * @param orderId 订单Id
     * @param payerId 卖家用户ID
     * @param amount 支付金额，BigDecimal转换本类提供了方法changePayAmount
     * @param expiration 过期时间
     * @return
     */
    @Override
    public PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, Long expiration) {
        return  createAppPreOrder(namespaceId, clientAppName, orderType, orderId, payerId, amount , null, null, expiration);
    }

    /**
     *
     * @param namespaceId 域空间
     * @param clientAppName 客户端realm值
     * @param orderType 订单类型 参考 com.everhomes.rest.order.OrderType
     * @param orderId 订单Id
     * @param payerId 卖家用户ID
     * @param amount 支付金额，BigDecimal转换本类提供了方法changePayAmount
     * @param resourceType 订单资源类型
     * @param resourceId  订单资源类型ID
     * @return
     */
    @Override
    public PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String resourceType, Long resourceId) {
        return  createAppPreOrder(namespaceId, clientAppName, orderType, orderId, payerId, amount , resourceType, resourceId, null);
    }

    /**
     *
     * @param namespaceId 域空间
     * @param clientAppName 客户端realm值
     * @param orderType 订单类型 参考 com.everhomes.rest.order.OrderType
     * @param orderId 订单Id
     * @param payerId 卖家用户ID
     * @param amount 支付金额，BigDecimal转换本类提供了方法changePayAmount
     * @param resourceType 订单资源类型
     * @param resourceId  订单资源类型ID
     * @param expiration 过期时间
     * @return
     */
    @Override
    public PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String resourceType, Long resourceId, Long expiration) {


        //app支付
        PreOrderCommand cmd = new PreOrderCommand();
        cmd.setClientAppName(clientAppName);
        cmd.setOrderType(orderType);
        cmd.setOrderId(orderId);
        cmd.setPayerId(payerId);
        cmd.setAmount(amount);
        cmd.setNamespaceId(namespaceId);
        cmd.setResourceType(resourceType);
        cmd.setResourceId(resourceId);
        cmd.setExpiration(expiration);

        LOGGER.info("createAppPreOrder cmd={}", cmd);
        return  createPreOrder(cmd);
    }

    /**
     *
     * @param namespaceId 域空间
     * @param clientAppName 客户端realm值
     * @param orderType 订单类型 参考 com.everhomes.rest.order.OrderType
     * @param orderId 订单Id
     * @param payerId 卖家用户ID
     * @param amount  支付金额，BigDecimal转换本类提供了方法changePayAmount
     * @param openid 微信用户的openId
     * @param paramsDTO 微信支付的参数
     * @return
     */
    @Override
    public PreOrderDTO createWxJSPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String openid, PaymentParamsDTO paramsDTO) {
        return  createWxJSPreOrder(namespaceId, clientAppName, orderType, orderId, payerId, amount, openid, paramsDTO, null, null, null);
    }

    /**
     *
     * @param namespaceId 域空间
     * @param clientAppName 客户端realm值
     * @param orderType 订单类型 参考 com.everhomes.rest.order.OrderType
     * @param orderId 订单Id
     * @param payerId 卖家用户ID
     * @param amount  支付金额，BigDecimal转换本类提供了方法changePayAmount
     * @param openid 微信用户的openId
     * @param paramsDTO 微信支付的参数
     * @param expiration 过期时间
     * @return
     */
    @Override
    public PreOrderDTO createWxJSPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String openid, PaymentParamsDTO paramsDTO, Long expiration) {
        return  createWxJSPreOrder(namespaceId, clientAppName, orderType, orderId, payerId, amount, openid, paramsDTO, null, null, expiration);
    }

    /**
     *
     * @param namespaceId 域空间
     * @param clientAppName 客户端realm值
     * @param orderType 订单类型 参考 com.everhomes.rest.order.OrderType
     * @param orderId 订单Id
     * @param payerId 卖家用户ID
     * @param amount  支付金额，BigDecimal转换本类提供了方法changePayAmount
     * @param openid 微信用户的openId
     * @param paramsDTO 微信支付的参数
     * @param resourceType  订单资源类型
     * @param resourceId 订单资源类型ID
     * @return
     */
    @Override
    public PreOrderDTO createWxJSPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount,  String openid, PaymentParamsDTO paramsDTO, String resourceType, Long resourceId) {
        return  createWxJSPreOrder(namespaceId, clientAppName, orderType, orderId, payerId, amount, openid, paramsDTO, resourceType, resourceId, null);
    }

    /**
     *
     * @param namespaceId 域空间
     * @param clientAppName 客户端realm值
     * @param orderType 订单类型 参考 com.everhomes.rest.order.OrderType
     * @param orderId 订单Id
     * @param payerId 卖家用户ID
     * @param amount  支付金额，BigDecimal转换本类提供了方法changePayAmount
     * @param openid 微信用户的openId
     * @param paramsDTO 微信支付的参数
     * @param resourceType  订单资源类型
     * @param resourceId 订单资源类型ID
     * @param expiration 过期时间
     * @return
     */
    @Override
    public PreOrderDTO createWxJSPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount,  String openid, PaymentParamsDTO paramsDTO, String resourceType, Long resourceId, Long expiration) {

        //app支付
        PreOrderCommand cmd = new PreOrderCommand();
        cmd.setClientAppName(clientAppName);
        cmd.setOrderType(orderType);
        cmd.setOrderId(orderId);
        cmd.setPayerId(payerId);
        cmd.setAmount(amount);
        cmd.setNamespaceId(namespaceId);
        cmd.setResourceType(resourceType);
        cmd.setResourceId(resourceId);
        cmd.setExpiration(expiration);
        cmd.setOpenid(openid);
        cmd.setPaymentType(PaymentType.WECHAT_JS_PAY.getCode());
        cmd.setPaymentParams(paramsDTO);

        return  createPreOrder(cmd);
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
     * @param cmd
     * @return
     */
    @Override
    public PreOrderDTO createPreOrder(PreOrderCommand cmd) {

        PreOrderDTO preOrderDTO = null;
        //1、查order表，如果order已经存在，则返回已有的合同，交易停止；否则，继续
        PaymentOrderRecord orderRecord = payProvider.findOrderRecordByOrder(cmd.getOrderType(), cmd.getOrderId());
        if (orderRecord != null) {
            preOrderDTO = recordToDto(orderRecord, cmd);
            return preOrderDTO;
        }

        //2、检查买方是否有会员，无则创建
        PaymentUser paymentUser = checkAndCreatePaymentUser(OwnerType.USER.getCode(), cmd.getPayerId());

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
    @Override
    public void payNotify(OrderPaymentNotificationCommand cmd) {

        //校验签名
        PaymentAccount paymentAccount = findPaymentAccount(SYSTEMID);
        if (paymentAccount == null) {
            LOGGER.error("payment account no find system_id={}", SYSTEMID);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_ACCOUNT_NO_FIND,
                    "payment account no find");
        }

        Map<String, String> params = new HashMap<>();
        com.everhomes.util.StringHelper.toStringMap(null, cmd, params);
        params.remove("signature");

        //TODO
        LOGGER.info("getSignature={}, mySignature={}", cmd.getSignature(), SignatureHelper.computeSignature(params, paymentAccount.getSecretKey()));

        if(!SignatureHelper.verifySignature(params, paymentAccount.getSecretKey(), cmd.getSignature())) {
            LOGGER.error("Notification signature verify fail");
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_SIGNATURE_VERIFY_FAIL,
                    "Notification signature verify fail");
        }


        //校验参数不为空

        if(cmd.getOrderId() == null||cmd.getPaymentStatus()==null || cmd.getBizOrderNum() == null || cmd.getOrderType() == null){

            LOGGER.error("Invalid parameter,orderId,orderType or paymentStatus is null");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter,orderId,orderType or paymentStatus is null");
        }


        //检查订单是否存在
        //TODO
        PaymentOrderRecord orderRecord = payProvider.findOrderRecordByOrderNum(cmd.getBizOrderNum());
        if(orderRecord == null){
            LOGGER.error("can not find order record by BizOrderNum={}", cmd.getBizOrderNum());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "can not find order record");
        }

        //此处将orderId设置成业务系统的orderid，方便业务调用。原orderId为支付系统的orderid，业务不需要知道。
        cmd.setOrderId(orderRecord.getOrderId());

        //调用具体业务
        LOGGER.info("Handler found# handler name = {}",String.valueOf(orderRecord.getOrderType()));
        PaymentCallBackHandler handler = this.getOrderHandler(String.valueOf(orderRecord.getOrderType()));
        LOGGER.debug("PaymentCallBackHandler="+handler.getClass().getName());

        SrvOrderPaymentNotificationCommand srvCmd = ConvertHelper.convert(cmd, SrvOrderPaymentNotificationCommand.class);
        srvCmd.setOrderType(orderRecord.getOrderType());
        com.everhomes.pay.order.OrderType orderType = com.everhomes.pay.order.OrderType.fromCode(cmd.getOrderType());
        if(orderType != null) {
            switch (orderType) {
                case PURCHACE:
                    if(cmd.getPaymentStatus()== OrderPaymentStatus.SUCCESS.getCode()){
                        //支付成功
                        handler.paySuccess(srvCmd);
                    }
                    if(cmd.getPaymentStatus()==OrderPaymentStatus.FAILED.getCode()){
                        //支付失败
                        handler.payFail(srvCmd);
                    }
                    break;
                case REFUND:
                    if(cmd.getPaymentStatus()== OrderPaymentStatus.SUCCESS.getCode()){
                        //退款成功
                        handler.refundSuccess(srvCmd);
                    }
                    if(cmd.getPaymentStatus()==OrderPaymentStatus.FAILED.getCode()){
                        //退款失败
                        handler.refundFail(srvCmd);
                    }
                    break;
                default:
                    LOGGER.error("unsupport orderType, orderType={}, cmd={}", orderType.getCode(), StringHelper.toJsonString(cmd));
            }
        }else {
            LOGGER.error("orderType is null, cmd={}", StringHelper.toJsonString(cmd));
        }

    }

    private PaymentCallBackHandler getOrderHandler(String orderType) {
        return PlatformContext.getComponent(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX+this.getOrderTypeCode(orderType));
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

    //检查买方会员，无则创建
    private PaymentUser checkAndCreatePaymentUser(String ownerType, Long ownerId){

        if(ownerType == null ||ownerId == null){
            LOGGER.error("Invalid parameter, ownerType or ownerId is null ownerType={}, ownerId={}", ownerType, ownerId);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_INVALID_USER_OWNER,
                    "invalid user owner type");
        }
        PaymentUser paymentUser = payProvider.findPaymentUserByOwner(ownerType, ownerId);
        if(paymentUser == null){
            paymentUser = createPaymentUser(BusinessUserType.PERSONAL.getCode(), ownerType, ownerId);

        }else if(paymentUser.getOwnerType() == null || OwnerType.fromCode(paymentUser.getOwnerType()) != OwnerType.USER){
            LOGGER.error("Invalid parameter ownerType={}", paymentUser.getOwnerType());
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_INVALID_USER_OWNER,
                    "invalid user owner type");
        }
        return paymentUser;
    }
    @Override
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
        payProvider.createPaymentUser(paymentUser);
        return paymentUser;
    }

    /**
     * 去支付系统创建用户
     * @param paymentUserId
     * @param phone
     * @throws Exception
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

    /**
     * 去支付系统创建用户
     * @param businessUserType
     * @param bizUserId
     * @return
     * @throws Exception
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
     * 检查卖方信息，无则报错
     * @param namespaceId
     * @param orderType
     * @param resourceType
     * @param resourceId
     * @return
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

    //获取
    private PaymentAccount findPaymentAccount(Integer systemId){
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
        createOrderCmd.setBizSystemId(paymentAccount.getSystemId());
        createOrderCmd.setClientAppName(cmd.getClientAppName());
        createOrderCmd.setPaymentType(cmd.getPaymentType());
        createOrderCmd.setValidationType(ValidationType.NO_VERIFY.getCode());
        createOrderCmd.setPaymentParams(flattenMap);
        createOrderCmd.setSettlementType(null);
        createOrderCmd.setSplitRuleId(serviceConfig.getPaymentSplitRuleId());
        if(cmd.getExpiration() != null) {
            createOrderCmd.setExpirationMillis(cmd.getExpiration());
        }
        //TODO 临时删除
        //createOrderCmd.setSummary(cmd.getSummary());
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
        createOrderCmd.setOrderRemark3(null);
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

//    public List<PayMethodDTO> listPayMethod() throws Exception {
//        List<PaymentType> paymentTypes = paymentTypeDao.listByCd(new ListPaymentTypeByCdCmd());
//
//        List<PayMethodDTO> payMethods = new ArrayList<PayMethodDTO>();
//        if(paymentTypes != null && !paymentTypes.isEmpty()) {
//            for(PaymentType r : paymentTypes) {
//                PayMethodDTO payMethodDto = ObjectConvertUtil.convertForSampleType(r, PayMethodDTO.class);
//                payMethodDto.setExtendInfo(getPayMethodExtendInfo());
//                payMethodDto.setPaymentLogo(imageUtil.populateToImageUrl(payMethodDto.getPaymentLogo(), ResourceConfigName.APP_USER_PAYMENT_TYPE_LOGO));
//                payMethods.add(payMethodDto);
//            }
//        }
//
//        return payMethods;
//    }

    private String getPayMethodExtendInfo(){
        String payV2HomeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.home.url", "");
        String getOrderInfoUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.orderPaymentStatusQueryUri", "");

        String format = "{\"getOrderInfoUrl\":\"%s\"}";
        return String.format(format, payV2HomeUrl+getOrderInfoUri);
    }


    /**
     * 获得特定长度的一串随机数字
     * @param length
     * @return
     * @throws
     */
    public static String getRandomNumberStr(int length){
        StringBuilder builder = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for(int i = 0 ; i < length; i++){
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }


    @Override
    public Long changePayAmount(BigDecimal amount){

        if(amount == null){
            return 0L;
        }
        return  amount.multiply(new BigDecimal(100)).longValue();
    }

    @Override
    public BigDecimal changePayAmount(Long amount){

        if(amount == null){
            return new BigDecimal(0);
        }
        return  new BigDecimal(amount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
    }

    @Override
    public CreateOrderRestResponse refund(String orderType, Long payOrderId, Long refundOrderId, Long amount) {
        PaymentOrderRecord payOrderRecord = payProvider.findOrderRecordByOrder(orderType, payOrderId);
        CreateOrderCommand cmd = newCreateOrderCommandForRefund(payOrderRecord, amount);
        CreateOrderRestResponse response = refuncOrderPayV2(cmd);

        //保存退款记录
        saveOrderRecord(response.getResponse(), refundOrderId, orderType, payOrderRecord.getServiceConfigId(), com.everhomes.pay.order.OrderType.REFUND.getCode());

        return response;
    }

    private CreateOrderRestResponse refuncOrderPayV2(CreateOrderCommand cmd){
        CreateOrderRestResponse cmdRestResponse = restClient.restCall(
                "POST", ApiConstants.ORDER_CREATEORDER_URL, cmd, CreateOrderRestResponse.class);
        System.out.println("createOrder (refund) response: " + StringHelper.toJsonString(cmdRestResponse));
        if(!cmdRestResponse.getErrorCode().equals(200)) {
            LOGGER.error("create order fail");
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_REFUND_FAIL,
                    "create order fail");
        }

        return cmdRestResponse;
    }

    private CreateOrderCommand newCreateOrderCommandForRefund(PaymentOrderRecord payOrderRecord, Long amount){
        CreateOrderCommand cmd = new CreateOrderCommand();
        cmd.setAmount(amount);
        cmd.setBizSystemId(SYSTEMID);
        cmd.setRefundOrderId(payOrderRecord.getPaymentOrderId());
        cmd.setOrderType(com.everhomes.pay.order.OrderType.REFUND.getCode());
        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
        String backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.callback.url", "");
        String backUrl = homeUrl + contextPath + backUri;
        cmd.setBackUrl(backUrl);
        cmd.setCommitFlag(1);
        //BizOrderNum需要传PaymentOrderRecords表记录的id，此处先申请id，在返回值中使用BizOrderNum做为record的id
        Long orderRecordId = payProvider.getNewPaymentOrderRecordId();
        cmd.setBizOrderNum(String.valueOf(orderRecordId));

        return cmd;
    }
    
    @Override
//    public PaymentBalanceDTO getPaymentSettlementAmounts(String ownerType, Long ownerId)  {
//        PaymentUser paymentUser = payProvider.findPaymentUserByOwner(ownerType, ownerId);
//        if(paymentUser == null) {
//            LOGGER.error("Payment user not found, ownerType=%s, ownerId=%s", ownerType, ownerId);
//            return null;
//        }
//
//        Long notSettledAmount = getPaymentAmountBySettlement(paymentUser.getPaymentUserId(), SettlementStatus.NOT_SETTLED.getCode());
//        Long settledAmount = getPaymentAmountBySettlement(paymentUser.getPaymentUserId(), SettlementStatus.SETTLED.getCode());
//        Long withdrawAmount = getPaymentAmountByWithdraw(paymentUser.getPaymentUserId());
//        Long refundAmount = getPaymentAmountByRefund(paymentUser.getPaymentUserId());
//        if(LOGGER.isDebugEnabled()) {
//            LOGGER.debug("Payment amounts info, notSettledAmount=%s, settledAmount=%s, withdrawAmount=%s, refundAmount=%s",
//                    notSettledAmount, settledAmount, withdrawAmount, refundAmount);
//        }
//
//        PaymentBalanceDTO result = new PaymentBalanceDTO();
//        result.setSettlementAmount(notSettledAmount);
//        result.setWithdrawableAmount(settledAmount - withdrawAmount - refundAmount);
//                
//        return result;
//    }
    public PaymentBalanceDTO getPaymentBalance(String ownerType, Long ownerId)  {
        PaymentUser paymentUser = payProvider.findPaymentUserByOwner(ownerType, ownerId);
        if(paymentUser == null) {
            LOGGER.error("Payment user not found, ownerType=%s, ownerId=%s", ownerType, ownerId);
            return null;
        }
        
        QueryBalanceCommand cmd = new QueryBalanceCommand();
        cmd.setUserId(paymentUser.getPaymentUserId());
        QueryBalanceRestResponse queryBalanceResp = restClient.restCall("POST", ApiConstants.ORDER_QUERYBALANCE_URL, cmd, QueryBalanceRestResponse.class);

        PaymentBalanceDTO result = new PaymentBalanceDTO();
        if(queryBalanceResp.getResponse() != null) {
            result.setAllAmount(queryBalanceResp.getResponse().getAllAmount());
            result.setFrozenAmount(queryBalanceResp.getResponse().getFrozenAmount());
            result.setUnsettledPaymentAmount(queryBalanceResp.getResponse().getUnsettledPaymentAmount());
            result.setUnsettledRechargeAmount(queryBalanceResp.getResponse().getUnsettledRechargeAmount());
            result.setUnsettledWithdrawAmount(queryBalanceResp.getResponse().getUnsettledWithdrawAmount());
            if(result.getAllAmount() != null && result.getFrozenAmount() != null && result.getUnsettledPaymentAmount() != null) {
                result.setWithdrawableAmount(result.getAllAmount() - result.getFrozenAmount() - result.getUnsettledPaymentAmount());
            }
        }
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Payment balance info, ownerType={}, paymentUserId={}, balance={}", ownerType, ownerId, paymentUser.getPaymentUserId(), result);
        }
        
        return result;
    }
    
    @Override
    public Long getPaymentAmountBySettlement(Long paymentUserId, Integer settlementStatus) {
        QueryOrderPaymentsCommand payCmd = new QueryOrderPaymentsCommand();
        QueryBuilder queryBuilder = payCmd.builder();
        queryBuilder.select(PaymentAttributes.AMOUNT.sum("amount"));

        // ORDERPAYMENT: 通过订单收到的款
        // FEECHARGE: 交易手续费（可正可负）
        // REFUND: 退款（可正可负，即含别人退给自己的，也含退给别人的）
        // REFUND_FEECHARGE: 退款费用（可正可负）
        QueryCondition condition = PaymentAttributes.TRASACTION_TYPE.eq(TransactionType.ORDERPAYMENT.getCode())
                .or(PaymentAttributes.TRASACTION_TYPE.eq(TransactionType.FEECHARGE.getCode()))
                .or(PaymentAttributes.TRASACTION_TYPE.eq(TransactionType.REFUND.getCode()))
                .or(PaymentAttributes.TRASACTION_TYPE.eq(TransactionType.REFUND_FEECHARGE.getCode()));

        condition = condition.and(PaymentAttributes.USER_ID.eq(paymentUserId))
                .and(PaymentAttributes.SETTLEMENT_STATUS.eq(settlementStatus))
                .and(PaymentAttributes.PAYMENT_STATUS.eq(com.everhomes.pay.order.OrderPaymentStatus.SUCCESS.getCode()));

        queryBuilder.where(condition);

        MapListRestResponse response = (MapListRestResponse) restClient.restCall(
                "POST",
                ApiConstants.ORDER_QUERYORDERPAYMENTS_URL,
                payCmd.done(),
                MapListRestResponse.class);

        if(response.getResponse() == null || response.getResponse().isEmpty()  || response.getResponse().get(0).get("amount") == null) {
            return 0L;
        } else {
            return Long.valueOf(response.getResponse().get(0).get("amount"));
        }
    }
    
    @Override
    public Long getPaymentAmountByWithdraw(Long paymentUserId) {
        QueryOrderPaymentsCommand payCmd = new QueryOrderPaymentsCommand();
        QueryBuilder queryBuilder = payCmd.builder();
        queryBuilder.select(PaymentAttributes.AMOUNT.sum("amount"));

        // WITHDRAW: 已提现
        QueryCondition condition = PaymentAttributes.TRASACTION_TYPE.eq(TransactionType.WITHDRAW.getCode())
                .and(PaymentAttributes.USER_ID.eq(paymentUserId))
                .and(PaymentAttributes.PAYMENT_STATUS.eq(com.everhomes.pay.order.OrderPaymentStatus.SUCCESS.getCode())
                        .or(PaymentAttributes.PAYMENT_STATUS.eq(com.everhomes.pay.order.OrderPaymentStatus.PENDING.getCode())));
        queryBuilder.where(condition);

        MapListRestResponse response = (MapListRestResponse) restClient.restCall(
                "POST",
                ApiConstants.ORDER_QUERYORDERPAYMENTS_URL,
                payCmd.done(),
                MapListRestResponse.class);

        if(response.getResponse() == null || response.getResponse().isEmpty() || response.getResponse().get(0).get("amount") == null) {
            return 0L;
        } else {
            return -Long.valueOf(response.getResponse().get(0).get("amount"));
        }
    }
    
    @Override
    public Long getPaymentAmountByRefund(Long paymentUserId) {
        QueryOrderPaymentsCommand payCmd = new QueryOrderPaymentsCommand();
        QueryBuilder queryBuilder = payCmd.builder();
        queryBuilder.select(PaymentAttributes.AMOUNT.sum("amount"));

        // REFUND: 已退款
        // REFUND_FEECHARGE: 退款手续费
        QueryCondition trasactionTypeCondition = PaymentAttributes.TRASACTION_TYPE.eq(TransactionType.REFUND.getCode())
                .or(PaymentAttributes.TRASACTION_TYPE.eq(TransactionType.REFUND_FEECHARGE.getCode()));
        
        QueryCondition condition = PaymentAttributes.USER_ID.eq(paymentUserId)
                .and(trasactionTypeCondition)
                .and(PaymentAttributes.PAYMENT_STATUS.eq(com.everhomes.pay.order.OrderPaymentStatus.SUCCESS.getCode()));
        queryBuilder.where(condition);

        MapListRestResponse response = (MapListRestResponse) restClient.restCall(
                "POST",
                ApiConstants.ORDER_QUERYORDERPAYMENTS_URL,
                payCmd.done(),
                MapListRestResponse.class);

        if(response.getResponse() == null || response.getResponse().isEmpty() || response.getResponse().get(0).get("amount") == null) {
            return 0L;
        } else {
            return -Long.valueOf(response.getResponse().get(0).get("amount"));
        }
    }
    
    public void withdraw(PaymentWithdrawCommand cmd) {
        String ownerType = cmd.getOwnerType();
        Long ownerId = cmd.getOwnerId();
        Long amount = cmd.getAmount();
        User operator = UserContext.current().getUser();
        
        withdraw(ownerType, ownerId, operator, amount);
    }
    
    public CreateOrderRestResponse withdraw(String ownerType, Long ownerId, User operator, Long amount) {
        if(amount == null || amount.longValue() <= 0L) {
            LOGGER.error("Invalid amount to withdraw, ownerType={}, ownerId={}, operatorUid={}, amount={}", 
                    ownerType, ownerId, operator.getId(), amount);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_INVALID_WITHDRAW_AMOUNT,
                    "Invalid amount to withdraw");
        }
        
        PaymentAccount paymentAccount = findPaymentAccount(SYSTEMID);
        if (paymentAccount == null) {
            LOGGER.error("Payment account no found, ownerType={}, ownerId={}, operatorUid={}, amount={}, systemId={}", 
                    ownerType, ownerId, operator.getId(), amount, SYSTEMID);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_ACCOUNT_NO_FIND,
                    "Payment account no found");
        }
        
        PaymentUser paymentUser = payProvider.findPaymentUserByOwner(ownerType, ownerId);
        if(paymentUser == null) {
            LOGGER.error("Withdraw account not found, ownerType={}, ownerId={}, operatorUid={}, amount={}, realAmount={}", 
                    ownerType, ownerId, operator.getId(), amount);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_WITHDRAW_ACCOUNT_NOT_FOUND,
                    "Withdraw account not found");
        }
        
        PaymentBalanceDTO paymentBalance = getPaymentBalance(ownerType, ownerId);
        Long withdrawableAmount = paymentBalance.getWithdrawableAmount();
        if(withdrawableAmount == null || withdrawableAmount.longValue() <= 0)  {
            LOGGER.error("Withdrawable amount insufficient, ownerType={}, ownerId={}, operatorUid={}, amount={}, realAmount={}", 
                    ownerType, ownerId, operator.getId(), amount, paymentBalance);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_WITHDRAWABLE_AMOUNT_INSUFFICIENT,
                    "Withdrawable amount insufficient");
        }
        
        Long orderNumber = createOrderNumberByTime();
        String orderType = OrderType.OrderTypeEnum.PRINT_ORDER.getPycode();
        Timestamp time = new Timestamp(DateHelper.currentGMTTime().getTime());
        PaymentWithdrawOrder order = new PaymentWithdrawOrder();
        order.setOwnerType(ownerType);
        order.setOwnerId(ownerId);
        order.setNamespaceId(operator.getNamespaceId());
        order.setOrderNo(orderNumber);
        order.setAmount(changePayAmount(amount));
        order.setPaymentUserType(paymentUser.getPaymentUserType());
        order.setPaymentUserId(paymentUser.getPaymentUserId());
        order.setOperatorUid(operator.getId());
        order.setOperateTime(time);
        order.setStatus(PaymentWithdrawOrderStatus.WAITING_FOR_CONFIRM.getCode());
        order.setCreatorUid(operator.getId());
        order.setCreateTime(time);
        payProvider.createPaymentWithdrawOrder(order);
        
        
        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
        String backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.callback.url", "");
        String backUrl = homeUrl + contextPath + backUri;
        String withdrawOrderNo = getOrderNum(order.getId(), OrderType.OrderTypeEnum.WITHDRAW_CODE.getPycode());
        CreateOrderCommand orderCmd = new CreateOrderCommand();
        orderCmd.setBizSystemId(paymentAccount.getSystemId());
        orderCmd.setAmount(amount);
        orderCmd.setBizOrderNum(withdrawOrderNo);
        orderCmd.setPayeeUserId(paymentUser.getPaymentUserId());
        orderCmd.setSourceType(SourceType.MOBILE.getCode());
        orderCmd.setOrderType(com.everhomes.pay.order.OrderType.WITHDRAW.getCode());
        orderCmd.setValidationType(ValidationType.NO_VERIFY.getCode());
        orderCmd.setBackUrl(backUrl);
        orderCmd.setCommitFlag(PaymentCommitFlag.YES.getCode());
        orderCmd.setPaymentType(PaymentType.WITHDRAW_AUTO.getCode());

        // 银行卡号和属性
        Map<String, String> paymentParams = new HashMap<>();
        paymentParams.put("bankCardNum", paymentUser.getBankCardNumber());
        paymentParams.put("bankCardPro", String.valueOf(getBankCardProperty(paymentUser.getPaymentUserType())));
        orderCmd.setPaymentParams(paymentParams);
        
        CreateOrderRestResponse createOrderResp = restClient.restCall(
                "POST", 
                ApiConstants.ORDER_CREATEORDER_URL, 
                orderCmd, 
                CreateOrderRestResponse.class);     
        
        if(CreateOrderRestResponse.isSuccess(createOrderResp)){
            Long serviceConfigId = 0L; // 提现时不分具体业务，故没有此ID信息
            OrderCommandResponse  orderCommandResponse = createOrderResp.getResponse();
            saveOrderRecord(orderCommandResponse, orderNumber, orderType,  serviceConfigId, com.everhomes.pay.order.OrderType.PURCHACE.getCode());
        } else {
            LOGGER.error("Failed to withdraw mony, ownerType={}, ownerId={}, operatorUid={}, amount={}, createOrderResp={}", 
                    ownerType, ownerId, operator.getId(), amount, StringHelper.toJsonString(createOrderResp));
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_WITHDRAWABLE_AMOUNT_INSUFFICIENT,
                    "Failed to withdraw mony");
        }
        
        return createOrderResp;
    }
    
    private int getBankCardProperty(Integer paymentUserType) {
        BusinessUserType userType = BusinessUserType.fromCode(paymentUserType);
        int bankCardPro = PaymentBankCardType.PERSONAL.getCode();
        if(userType == BusinessUserType.BUSINESS) {
            bankCardPro = PaymentBankCardType.BUSINESS.getCode();
        }
        
        return bankCardPro;
    }
    
    public Long createOrderNumberByTime() {
        String suffix = String.valueOf(generateRandomNumber(3));

        return Long.valueOf(String.valueOf(System.currentTimeMillis()) + suffix);
    }

    /**
     *
     * @param n 创建n位随机数
     * @return
     */
    private long generateRandomNumber(int n){
        return (long)((Math.random() * 9 + 1) * Math.pow(10, n-1)); 
    }
    
    @Override
    public ListPaymentWithdrawOrderResponse listPaymentWithdrawOrders(ListPaymentWithdrawOrderCommand cmd) {
        String ownerType = cmd.getOwnerType();
        Long ownerId = cmd.getOwnerId();
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        List<PaymentWithdrawOrder> orders = payProvider.listPaymentWithdrawOrders(ownerType, ownerId, cmd.getPageAnchor(), pageSize+1);
        
        ListPaymentWithdrawOrderResponse response = new ListPaymentWithdrawOrderResponse();
        if(orders.size() > pageSize) {
            orders.remove(orders.size() - 1);
            response.setNextPageAnchor(orders.get(orders.size() - 1).getId());
        }
        response.setOrders(toPaymentWithdrawOrderDTO(orders));
        
        return response;
    }
    
    private List<PaymentWithdrawOrderDTO> toPaymentWithdrawOrderDTO(List<PaymentWithdrawOrder> orders) {
        List<PaymentWithdrawOrderDTO> dtos = new ArrayList<PaymentWithdrawOrderDTO>();
        for(PaymentWithdrawOrder order : orders) {
            dtos.add(toPaymentWithdrawOrderDTO(order));
        }
        
        return dtos;
    }
    
    private PaymentWithdrawOrderDTO toPaymentWithdrawOrderDTO(PaymentWithdrawOrder order) {
        PaymentWithdrawOrderDTO orderDto = ConvertHelper.convert(order, PaymentWithdrawOrderDTO.class);
        
        // 在数据库取出来时amount是BigDecimal类型，给客户端时则是Long型，故需要特殊转一下
        Long amount = changePayAmount(order.getAmount());
        orderDto.setAmount(amount);
        
        UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(order.getOperatorUid(), order.getNamespaceId());
        if(userIdentifier != null) {
            orderDto.setOperatorPhone(userIdentifier.getIdentifierToken());
        } else {
            LOGGER.error("User identifier not found, ownerType={}, ownerId={}, withdrawOrderId={}, operatorUid={}", 
                        order.getOwnerType(), order.getOwnerId(), order.getId(), order.getOperatorUid());
        }
        
        User user = userProvider.findUserById(order.getOperatorUid());
        if(user != null) {
            orderDto.setOperatorName(user.getNickName());
        } else {
            LOGGER.error("User not found, ownerType={}, ownerId={}, withdrawOrderId={}, operatorUid={}", 
                    order.getOwnerType(), order.getOwnerId(), order.getId(), order.getOperatorUid());
        }
        
        OwnerType  ownerType = OwnerType.fromCode(order.getOwnerType());
        if(ownerType == OwnerType.ORGANIZATION) {
            OrganizationMemberDetails orgMember = organizationProvider.findOrganizationMemberDetailsByTargetId(order.getOperatorUid());
            if(orgMember != null && orgMember.getContactName() != null && orgMember.getContactName().length() > 0) {
                orderDto.setEnterpriseName(orgMember.getContactName());
            } else {
                LOGGER.error("Organization not found, ownerType={}, ownerId={}, withdrawOrderId={}", 
                        order.getOwnerType(), order.getOwnerId(), order.getId());
            }
        }
        
        return orderDto;
    }
}
