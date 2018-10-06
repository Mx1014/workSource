package com.everhomes.payment;

import com.everhomes.asset.PaymentConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.pay.order.OrderCommandResponse;
import com.everhomes.pay.order.SourceType;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.promotion.order.controller.CreatePurchaseOrderRestResponse;
import com.everhomes.rest.promotion.order.controller.CreateRefundOrderRestResponse;
import com.everhomes.rest.promotion.order.*;
import com.everhomes.rest.order.*;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.payment.CardRechargeStatus;
import com.everhomes.rest.payment.GetAccountSettingCommand;
import com.everhomes.rest.payment.PaymentCardStatus;
import com.everhomes.rest.payment.UpdateAccountSettingCommand;
import com.everhomes.paySDK.PayUtil;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.rentalv2.RentalServiceErrorCode;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.everhomes.organization.pm.pay.GsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PaymentCardPayServiceImpl implements  PaymentCardPayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardPayServiceImpl.class);

    @Autowired
    private com.everhomes.paySDK.api.PayService payServiceV2;
    @Autowired
    private PaymentCardProvider paymentCardProvider;
    @Autowired
    protected GeneralOrderService orderService;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Value("${server.contextPath:}")
    private String contextPath;
    @Override
    public List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd) {
        String userPrefix = "EhOrganizations";
        List<String> list = new ArrayList<>();
        list.add("0");
        if (cmd.getCommunityId() != null)
            list.add(cmd.getCommunityId().toString());
        List<PayUserDTO> payUserDTOs = payServiceV2.getPayUserList(userPrefix + cmd.getOrganizationId(), list);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("List rental payee accounts(response), orgnizationId={}, tags={}, response={}", cmd.getOrganizationId(), cmd.getCommunityId(), GsonUtil.toJson(payUserDTOs));
        }

        List<ListBizPayeeAccountDTO> result = new ArrayList<ListBizPayeeAccountDTO>();
        if (payUserDTOs != null) {
            for (PayUserDTO payUserDTO : payUserDTOs) {
                ListBizPayeeAccountDTO dto = convertAccount(payUserDTO);
                result.add(dto);
            }
        }
        return result;
    }

    private ListBizPayeeAccountDTO convertAccount(PayUserDTO payUserDTO){
        if (payUserDTO == null)
            return null;
        ListBizPayeeAccountDTO dto = new ListBizPayeeAccountDTO();
        // 支付系统中的用户ID
        dto.setAccountId(payUserDTO.getId());
        // 用户向支付系统注册帐号时填写的帐号名称
        dto.setAccountName(payUserDTO.getRemark());
        dto.setAccountAliasName(payUserDTO.getUserAliasName());//企业名称（认证企业）
        // 帐号类型，1-个人帐号、2-企业帐号
        Integer userType = payUserDTO.getUserType();
        if (userType != null && userType.equals(2)) {
            dto.setAccountType(OwnerType.ORGANIZATION.getCode());
        } else {
            dto.setAccountType(OwnerType.USER.getCode());
        }
        // 企业账户：0未审核 1审核通过  ; 个人帐户：0 未绑定手机 1 绑定手机
        Integer registerStatus = payUserDTO.getRegisterStatus();
        if (registerStatus != null && registerStatus.intValue() == 1) {
            dto.setAccountStatus(PaymentUserStatus.ACTIVE.getCode());
        } else {
            dto.setAccountStatus(PaymentUserStatus.WAITING_FOR_APPROVAL.getCode());
        }
        return  dto;
    }

    @Override
    public ListBizPayeeAccountDTO getAccountSetting(GetAccountSettingCommand cmd) {
        List<PaymentCardAccount> accounts = paymentCardProvider.listPaymentCardAccounts(cmd.getOwnerType(), cmd.getOwnerId());
        if (accounts == null || accounts.size() == 0)
            return null;
        List<PayUserDTO> payUserDTOs = payServiceV2.listPayUsersByIds(accounts.stream().map(r -> r.getAccountId()).collect(Collectors.toList()));
        if (payUserDTOs == null || payUserDTOs.size() == 0)
            return null;
        ListBizPayeeAccountDTO dto = convertAccount(payUserDTOs.get(0));

        return dto;
    }

    @Override
    public void updateAccountSetting(UpdateAccountSettingCommand cmd) {
        //删除
        paymentCardProvider.deleteAccounts(cmd.getOwnerType(),cmd.getOwnerId());
        //添加
        PaymentCardAccount account = ConvertHelper.convert(cmd,PaymentCardAccount.class);
        account.setNamespaceId(UserContext.getCurrentNamespaceId());
        paymentCardProvider.createPaymentCardAccount(account);

    }

    @Override
    public PreOrderDTO createPreOrder(PreOrderCommand cmd,PaymentCardRechargeOrder order) {
        PreOrderDTO preOrderDTO = null;
        //1、收款方是否有会员，无则报错
        cmd.setBizPayeeId(getPayeeAccountByCommunityId(cmd.getCommunityId()));
        List<PayUserDTO> payUserDTOs = payServiceV2.listPayUsersByIds(Stream.of(cmd.getBizPayeeId()).collect(Collectors.toList()));
        if (payUserDTOs == null || payUserDTOs.size() == 0){
            LOGGER.error("payeeUserId no find, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, 1001,
                    "暂未绑定收款账户");
        }
        //2、组装报文，发起下单请求
        PurchaseOrderCommandResponse orderCommandResponse = createOrder(cmd);

        //3、组装支付方式
        preOrderDTO = orderCommandResponseToDto(orderCommandResponse, cmd);

        //4、保存订单信息
        saveOrderRecord( order,orderCommandResponse);
        return preOrderDTO;
    }

    private void saveOrderRecord(PaymentCardRechargeOrder order , PurchaseOrderCommandResponse orderCommandResponse) {
        OrderCommandResponse response = orderCommandResponse.getPayResponse();
        order.setBizOrderNo(response.getBizOrderNum());
        paymentCardProvider.updatePaymentCardRechargeOrder(order);
    }

    private PreOrderDTO orderCommandResponseToDto(PurchaseOrderCommandResponse orderCommandResponse, PreOrderCommand cmd){
        OrderCommandResponse response = orderCommandResponse.getPayResponse();
        PreOrderDTO dto = ConvertHelper.convert(response, PreOrderDTO.class);
        dto.setAmount(cmd.getAmount());
        List<com.everhomes.pay.order.PayMethodDTO> paymentMethods = response.getPaymentMethods();
        if (paymentMethods != null)
            dto.setPayMethod(paymentMethods.stream().map(r->{
                PayMethodDTO convert = ConvertHelper.convert(r, PayMethodDTO.class);
                String paymentLogo = contentServerService.parserUri(r.getPaymentLogo());
                convert.setPaymentLogo(paymentLogo);
                if (r.getPaymentParams() != null) {
                    PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
                    paymentParamsDTO.setPayType(r.getPaymentParams().getPayType());
                    convert.setPaymentParams(paymentParamsDTO);
                }
                convert.setExtendInfo(getPayMethodExtendInfo());
                return convert;
            }).collect(Collectors.toList()));
        dto.setOrderId(cmd.getOrderId());
        return dto;
    }

    private String getPayMethodExtendInfo(){
        String payV2HomeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.payHomeUrl", "");
        String getOrderInfoUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.orderPaymentStatusQueryUri", "");

        String format = "{\"getOrderInfoUrl\":\"%s\"}";
        return String.format(format, payV2HomeUrl+getOrderInfoUri);
    }

    private PurchaseOrderCommandResponse createOrder(PreOrderCommand preOrderCommand){
        CreatePurchaseOrderCommand createOrderCommand = preparePaymentBillOrder(preOrderCommand);
        CreatePurchaseOrderRestResponse createOrderResp = orderService.createPurchaseOrder(createOrderCommand);
        if(!checkOrderRestResponseIsSuccess(createOrderResp)) {
            String scope = OrderErrorCode.SCOPE;
            int code = OrderErrorCode.ERROR_CREATE_ORDER_FAILED;
            String description = "Failed to create order";
            if(createOrderResp != null) {
                code = (createOrderResp.getErrorCode() == null) ? code : createOrderResp.getErrorCode()  ;
                scope = (createOrderResp.getErrorScope() == null) ? scope : createOrderResp.getErrorScope();
                description = (createOrderResp.getErrorDescription() == null) ? description : createOrderResp.getErrorDescription();
            }
            throw RuntimeErrorException.errorWith(scope, code, description);
        }

        PurchaseOrderCommandResponse orderCommandResponse = createOrderResp.getResponse();
        return orderCommandResponse;
    }

    private CreatePurchaseOrderCommand preparePaymentBillOrder(PreOrderCommand cmd) {
        CreatePurchaseOrderCommand preOrderCommand = new CreatePurchaseOrderCommand();
        //PaymentParamsDTO转为Map
        Map<String, String> flattenMap = new HashMap<>();
        StringHelper.toStringMap(null, cmd.getPaymentParams(), flattenMap);

        preOrderCommand.setAmount(cmd.getAmount());
        String accountCode = "NS"+cmd.getNamespaceId();
        preOrderCommand.setAccountCode(accountCode);
        preOrderCommand.setClientAppName(cmd.getClientAppName());

        preOrderCommand.setBusinessOrderType(cmd.getOrderType());
        BusinessPayerType payerType = BusinessPayerType.USER;
        preOrderCommand.setBusinessPayerType(payerType.getCode());
        preOrderCommand.setBusinessPayerId(String.valueOf(UserContext.currentUserId()));
        String businessPayerParams = getBusinessPayerParams(cmd);
        preOrderCommand.setBusinessPayerParams(businessPayerParams);

        preOrderCommand.setPaymentPayeeId(cmd.getBizPayeeId());
        preOrderCommand.setPaymentParams(flattenMap);

        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
        String backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.callback.url.paymentCard", "");
        String backUrl = homeUrl + contextPath + backUri;
        preOrderCommand.setCallbackUrl(backUrl);
        preOrderCommand.setExtendInfo(cmd.getExtendInfo());
        preOrderCommand.setGoodsName("一卡通");
        preOrderCommand.setGoodsDescription(null);
        preOrderCommand.setIndustryName(null);
        preOrderCommand.setIndustryCode(null);
        preOrderCommand.setSourceType(SourceType.MOBILE.getCode());
        preOrderCommand.setOrderRemark1("一卡通");
        preOrderCommand.setOrderRemark3(String.valueOf(cmd.getCommunityId()));
        preOrderCommand.setOrderRemark4(null);
        preOrderCommand.setOrderRemark5(null);
        String systemId = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), PaymentConstants.KEY_SYSTEM_ID, "");
        preOrderCommand.setBusinessSystemId(Long.parseLong(systemId));

        return preOrderCommand;

    }

    private String getBusinessPayerParams(PreOrderCommand cmd) {

        Long businessPayerId = UserContext.currentUserId();


        UserIdentifier buyerIdentifier = userProvider.findUserIdentifiersOfUser(businessPayerId, cmd.getNamespaceId());
        String buyerPhone = null;
        if(buyerIdentifier != null) {
            buyerPhone = buyerIdentifier.getIdentifierToken();
        }
        // 找不到手机号则默认一个
        if(buyerPhone == null || buyerPhone.trim().length() == 0) {
            buyerPhone = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), PaymentConstants.KEY_ORDER_DEFAULT_PERSONAL_BIND_PHONE, "");
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("businessPayerPhone", buyerPhone);
        return StringHelper.toJsonString(map);
    }

    /*
     * 由于从支付系统里回来的CreateOrderRestResponse有可能没有errorScope，故不能直接使用CreateOrderRestResponse.isSuccess()来判断，
       CreateOrderRestResponse.isSuccess()里会对errorScope进行比较
     */
    private boolean checkOrderRestResponseIsSuccess(CreatePurchaseOrderRestResponse response){
        if(response != null && response.getErrorCode() != null
                && (response.getErrorCode().intValue() == 200 || response.getErrorCode().intValue() == 201))
            return true;
        return false;
    }

    private Long getPayeeAccountByCommunityId(Long communityId){
        List<PaymentCardAccount> accounts = paymentCardProvider.listPaymentCardAccounts(EntityType.COMMUNITY.getCode(),communityId);
        if (accounts != null && accounts.size() > 0)
            return accounts.get(0).getAccountId();
        return null;
    }

    @Override
    public void refundOrder(PaymentCardRechargeOrder order) {
        Long amount = order.getAmount().multiply(new BigDecimal(100)).longValue();
        CreateRefundOrderCommand createRefundOrderCommand = new CreateRefundOrderCommand();
        String systemId = configurationProvider.getValue(0, PaymentConstants.KEY_SYSTEM_ID, "");
        createRefundOrderCommand.setBusinessSystemId(Long.parseLong(systemId));
        String sNamespaceId = "NS"+UserContext.getCurrentNamespaceId();
        createRefundOrderCommand.setAccountCode(sNamespaceId);
        createRefundOrderCommand.setBusinessOrderNumber(order.getBizOrderNo());
        createRefundOrderCommand.setAmount(amount);
        createRefundOrderCommand.setBusinessOperatorType(BusinessPayerType.USER.getCode());
        createRefundOrderCommand.setBusinessOperatorId(String.valueOf(UserContext.currentUserId()));
        String homeurl = configurationProvider.getValue("home.url", "");
        String callbackurl = configurationProvider.getValue("pay.v2.refund.url.paymentCard", "/payment/refundNotify");
        callbackurl = homeurl + contextPath + callbackurl;
        createRefundOrderCommand.setCallbackUrl(callbackurl);
        createRefundOrderCommand.setSourceType(SourceType.MOBILE.getCode());
        CreateRefundOrderRestResponse refundOrderRestResponse = this.orderService.createRefundOrder(createRefundOrderCommand);

        if(refundOrderRestResponse != null && refundOrderRestResponse.getErrorCode() != null && refundOrderRestResponse.getErrorCode().equals(HttpStatus.OK.value())){

        } else{
            LOGGER.error("Refund failed from vendor, orderId={}, response={}",
                    order.getId(), refundOrderRestResponse);
            throw RuntimeErrorException.errorWith(OrderErrorCode.SCOPE,
                    RentalServiceErrorCode.ERROR_REFUND_ERROR,
                    "bill refund error");
        }

        order.setPayStatus(CardRechargeStatus.REFUNDING.getCode());
        paymentCardProvider.updatePaymentCardRechargeOrder(order);

    }
}
