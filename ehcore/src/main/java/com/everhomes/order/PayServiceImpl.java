//@formatter:off
package com.everhomes.order;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.pay.base.RestClient;
import com.everhomes.pay.order.*;
import com.everhomes.pay.rest.ApiConstants;
import com.everhomes.rest.order.*;
import com.everhomes.rest.order.OrderPaymentStatus;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PayServiceImpl implements PayService {

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

    private RestClient restClient = null;

    @PostConstruct
    void init() throws Exception {
        String payHomeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.home.url", "");
        PaymentAccount paymentAccount = findPaymentAccount(SYSTEMID);
        restClient = new RestClient(payHomeUrl, paymentAccount.getAppKey(), paymentAccount.getSecretKey());
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

        //防止同样的订单并发请求两次，加了锁之后会第二个请求会被阻塞，当它拿到锁之后订单已存在然后直接获取返回
        String lockName = CoordinationLocks.PAY_CREATE_PREORDER.getCode() + cmd.getOrderType() + cmd.getOrderId();
        PreOrderDTO dto = coordinationProvider.getNamedLock(lockName).enter(()-> {
                    PreOrderDTO preOrderDTO = null;
                    //1、查order表，如果order已经存在，则返回已有的合同，交易停止；否则，继续
                    PaymentOrderRecord orderRecord = payProvider.findOrderRecordByOrder(cmd.getOrderType(), cmd.getOrderId());
                    if (orderRecord != null) {
                        preOrderDTO = recordToDto(orderRecord, cmd);
                        return preOrderDTO;
                    }

                    //2、检查买方是否有会员，无则创建
                    PaymentUser paymentUser = checkAndCreatePaymentUser(cmd.getOwnerType(), cmd.getOwnerId());

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
                    preOrderDTO = orderCommandResponseToDto(orderCommandResponse, cmd);

                    //7、保存订单信息
                    saveOrderRecord(cmd, orderCommandResponse, serviceConfig);

                    //8、返回
                    return preOrderDTO;
                }).first();

        //8、返回
        return dto;

    }
    @Override
    public void payNotify(OrderPaymentNotificationCommand cmd) {
        if(cmd.getOrderId() == null||cmd.getPaymentStatus()==null||cmd.getPaymentType()==null){
            LOGGER.error("Invalid parameter,orderId,orderType or paymentStatus is null");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter,orderId,orderType or paymentStatus is null");
        }
        PaymentCallBackHandler handler = this.getOrderHandler(String.valueOf(cmd.getPaymentType()));
        LOGGER.debug("PaymentCallBackHandler="+handler.getClass().getName());
        if(cmd.getPaymentStatus()== OrderPaymentStatus.SUCCESS.getCode()){
            handler.paySuccess(cmd);
        }
        if(cmd.getPaymentStatus()==OrderPaymentStatus.FAILED.getCode()){
            handler.payFail(cmd);
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

        PaymentUser paymentUser = payProvider.findPaymentUserByOwner(ownerType, ownerId);
        if(paymentUser == null){
            //TODO 创建用户

        }else if(paymentUser.getOwnerType() == null || OwnerType.fromCode(paymentUser.getOwnerType()) != OwnerType.USER){
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_INVALID_USER_OWNER_TYPE,
                    "invalid user owner type");
        }
        return paymentUser;
    }


    //检查卖方信息，无则报错
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
        PaymentServiceConfigHandler handler = PlatformContext.getComponent(PaymentServiceConfigHandler.PAYMENT_SERVICE_CONFIG_HANDLER_PREFIX +this.getOrderTypeCode(orderType));

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
        CreateOrderCommand createOrderCommand = newCreateOrderCommand(preOrderCommand, paymentUser, serviceConfig, paymentAccount);

        OrderCommandResponse response = null;

        try {

            CreateOrderRestResponse createOrderResp = createOrderPayV2(createOrderCommand);
            if(!createOrderResp.getErrorCode().equals(200)) {
                LOGGER.error("create order fail");
                throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
                        "create order fail");
            }
            response = createOrderResp.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private CreateOrderCommand newCreateOrderCommand(PreOrderCommand cmd, PaymentUser paymentUser, PaymentServiceConfig serviceConfig, PaymentAccount paymentAccount){

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
            createOrderCmd.setExpiration(new Timestamp(cmd.getExpiration()));
        }
        createOrderCmd.setSummary(cmd.getSummary());
        createOrderCmd.setPayeeUserId(serviceConfig.getPaymentUserId());
        createOrderCmd.setPayerUserId(paymentUser.getPaymentUserId());
        createOrderCmd.setFrontUrl(null);

        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
        String backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.callback.url", "");
        String backUrl = homeUrl + contextPath + backUri;

        createOrderCmd.setBackUrl(backUrl);
        createOrderCmd.setExtendInfo(cmd.getExtendInfo());
        createOrderCmd.setGoodsName(null);
        createOrderCmd.setGoodsDescription(null);
        createOrderCmd.setIndustryCode(null);
        createOrderCmd.setIndustryName(null);
        createOrderCmd.setSourceType(SourceType.MOBILE.getCode());

        //BizOrderNum需要传PaymentOrderRecords表记录的id，此处先申请id，在返回值中使用BizOrderNum做为record的id
        Long orderRecordId = payProvider.getNewPaymentOrderRecordId();
        createOrderCmd.setBizOrderNum(String.valueOf(orderRecordId));

        createOrderCmd.setOrderRemark1(String.valueOf(serviceConfig.getId()));

        createOrderCmd.setOrderRemark2(null);
        createOrderCmd.setOrderRemark3(null);
        createOrderCmd.setOrderRemark4(null);
        createOrderCmd.setOrderRemark5(null);
        createOrderCmd.setCommitFlag(null);

        return createOrderCmd;
    }

    private CreateOrderRestResponse createOrderPayV2(CreateOrderCommand cmd) throws Exception {
        if(LOGGER.isDebugEnabled()) {LOGGER.debug("createOrderPayV2-command=" + GsonUtil.toJson(cmd));}
        CreateOrderRestResponse response = restClient.restCall(
                "POST",
                ApiConstants.ORDER_CREATEORDER_URL,
                cmd,
                CreateOrderRestResponse.class);
        if(LOGGER.isDebugEnabled()) {LOGGER.debug("createOrderPayV2-response=" + GsonUtil.toJson(response));}
        return response;
    }


    private void saveOrderRecord(PreOrderCommand preOrderCommand, OrderCommandResponse orderCommandResponse, PaymentServiceConfig serviceConfig){

        PaymentOrderRecord record = ConvertHelper.convert(orderCommandResponse, PaymentOrderRecord.class);

        //下预付单时，BizOrderNum需要传PaymentOrderRecords表记录的id，此处先申请id，在返回值中使用BizOrderNum做为record的id
        record.setId(Long.valueOf(orderCommandResponse.getBizOrderNum()));

        record.setOrderId(preOrderCommand.getOrderId());
        //PaymentOrderId为支付系统传来的orderId
        record.setPaymentOrderId(orderCommandResponse.getOrderId());
        record.setServiceConfigId(serviceConfig.getId());
        record.setOrderType(serviceConfig.getOrderType());
        payProvider.createPaymentOrderRecord(record);
    }

    private PreOrderDTO recordToDto(PaymentOrderRecord record, PreOrderCommand cmd){
        PreOrderDTO dto = ConvertHelper.convert(record, PreOrderDTO.class);

        dto.setAmount(cmd.getAmount());
        dto.setExtendInfo(cmd.getExtendInfo());

        List<PayMethodDTO> payMethods = listPayMethods(cmd.getNamespaceId(), cmd.getOrderType(),
                cmd.getOwnerType(), cmd.getOwnerId(), cmd.getResourceType(), cmd.getResourceId());
        dto.setPayMethod(payMethods);
        return dto;
    }

    private PreOrderDTO orderCommandResponseToDto(OrderCommandResponse orderCommandResponse, PreOrderCommand cmd){
        PreOrderDTO dto = ConvertHelper.convert(orderCommandResponse, PreOrderDTO.class);
        dto.setAmount(cmd.getAmount());
        List<PayMethodDTO> payMethods = listPayMethods(cmd.getNamespaceId(), cmd.getOrderType(),
                cmd.getOwnerType(), cmd.getOwnerId(), cmd.getResourceType(), cmd.getResourceId());
        dto.setPayMethod(payMethods);
        return dto;
    }

    private List<PayMethodDTO> listPayMethods(Integer namespaceId, String orderType, String ownerType, Long ownerId, String resourceType, Long resourceId){
        List<PayMethodDTO> payMethods = payProvider.listPayMethods(namespaceId, orderType, ownerType, ownerId, resourceType, resourceId);
        if(payMethods != null && payMethods.size() > 0){
            for(PayMethodDTO r : payMethods) {
                PaymentExtendInfo extendInfo = new PaymentExtendInfo();
                extendInfo.setGetOrderInfoUrl(getPayMethodExtendInfo());
                r.setExtendInfo(extendInfo);

                //TODO paymentParams和Logo暂时不管了
                //r.setPaymentParams();
                //r.setPaymentLogo(imageUtil.populateToImageUrl(payMethodDto.getPaymentLogo(), ResourceConfigName.APP_USER_PAYMENT_TYPE_LOGO));
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


}
