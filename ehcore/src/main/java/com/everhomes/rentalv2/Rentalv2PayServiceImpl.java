package com.everhomes.rentalv2;

import com.everhomes.asset.PaymentConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.FlowAutoStepDTO;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.flow.*;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.order.PaymentServiceConfig;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.pay.order.*;
import com.everhomes.paySDK.PaySettings;
import com.everhomes.paySDK.PayUtil;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.promotion.merchant.GetPayAccountByMerchantIdCommand;
import com.everhomes.rest.promotion.merchant.GetPayUserListByMerchantCommand;
import com.everhomes.rest.promotion.merchant.GetPayUserListByMerchantDTO;
import com.everhomes.rest.promotion.merchant.ListPayUsersByMerchantIdsCommand;
import com.everhomes.rest.promotion.merchant.controller.GetMerchantListByPayUserIdRestResponse;
import com.everhomes.rest.promotion.merchant.controller.GetPayAccountByMerchantIdRestResponse;
import com.everhomes.rest.promotion.merchant.controller.ListPayUsersByMerchantIdsRestResponse;
import com.everhomes.rest.promotion.order.controller.CreateMerchantOrderRestResponse;
import com.everhomes.rest.promotion.order.controller.CreatePurchaseOrderRestResponse;
import com.everhomes.rest.promotion.order.controller.CreateRefundOrderRestResponse;
import com.everhomes.rest.promotion.order.*;
import com.everhomes.rest.order.*;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.rest.rentalv2.PreOrderCommand;
import com.everhomes.rest.rentalv2.admin.*;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Rentalv2PayServiceImpl implements Rentalv2PayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Rentalv2PayServiceImpl.class);
    private static final String REFER_TYPE= FlowReferType.RENTAL.getCode();
    public static final Long moduleId = 40400L;
    @Autowired
    private com.everhomes.paySDK.api.PayService payServiceV2;
    @Autowired
    private Rentalv2AccountProvider rentalv2AccountProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private Rentalv2Provider rentalProvider;
    @Autowired
    private Rentalv2Service rentalService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private FlowCaseProvider flowCaseProvider;
    @Autowired
    private SmsProvider smsProvider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Value("${server.contextPath:}")
    private String contextPath;
    @Autowired
    protected GeneralOrderService orderService;

    @Override
    public List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd) {
        //String userPrefix = "EhBizBusinesses";
        String userPrefix = "EhOrganizations";
        List<String> list = new ArrayList<>();
        list.add("0");
        if (cmd.getCommunityId() != null)
             list.add(cmd.getCommunityId().toString());
        GetPayUserListByMerchantCommand cmd2 = new GetPayUserListByMerchantCommand();
        cmd2.setUserId(userPrefix + cmd.getOrganizationId());
        cmd2.setTag1(list);
        GetMerchantListByPayUserIdRestResponse restResponse = orderService.getMerchantListByPayUserId(cmd2);
        List<GetPayUserListByMerchantDTO> merchantDTOS = restResponse.getResponse();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("List rental payee accounts(response), orgnizationId={}, tags={}, response={}", cmd.getOrganizationId(), cmd.getCommunityId(), GsonUtil.toJson(merchantDTOS));
        }

        List<ListBizPayeeAccountDTO> result = new ArrayList<ListBizPayeeAccountDTO>();
        if (merchantDTOS != null) {
            for (GetPayUserListByMerchantDTO merchantDTO : merchantDTOS) {
                PayUserDTO payUserDTO = ConvertHelper.convert(merchantDTO,PayUserDTO.class);
                ListBizPayeeAccountDTO dto = convertAccount(payUserDTO);
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public ListBizPayeeAccountDTO getGeneralAccountSetting(GetGeneralAccountSettingCommand cmd) {
        List<Rentalv2PayAccount> accounts = this.rentalv2AccountProvider.listPayAccounts(UserContext.getCurrentNamespaceId(), cmd.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                null,RuleSourceType.DEFAULT.getCode(), cmd.getResourceTypeId(), null, null);
        if (accounts == null || accounts.size() == 0)
            return null;

        ListPayUsersByMerchantIdsCommand cmd2 = new ListPayUsersByMerchantIdsCommand();
        cmd2.setIds(accounts.stream().map(r -> r.getMerchantId()).collect(Collectors.toList()));
        ListPayUsersByMerchantIdsRestResponse restResponse = orderService.listPayUsersByMerchantIds(cmd2);
        List<PayUserDTO> payUserDTOs = restResponse.getResponse();
        if (payUserDTOs == null || payUserDTOs.size() == 0)
            return null;
        ListBizPayeeAccountDTO dto = convertAccount(payUserDTOs.get(0));

        return dto;
    }

    @Override
    public void updateGeneralAccountSetting(UpdateGeneralAccountSettingCommand cmd) {
        //删除
        rentalv2AccountProvider.deletePayAccount(null,cmd.getCommunityId(),RuleSourceType.DEFAULT.getCode(),cmd.getResourceTypeId());
        //添加
        Rentalv2PayAccount account = new Rentalv2PayAccount();
        account.setMerchantId(cmd.getAccountId());//支付账号换成商户id
        account.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
        account.setCommunityId(cmd.getCommunityId());
        account.setNamespaceId(UserContext.getCurrentNamespaceId());
        account.setSourceType(RuleSourceType.DEFAULT.getCode());
        account.setSourceId(cmd.getResourceTypeId());
        rentalv2AccountProvider.createPayAccount(account);
    }

    @Override
    public GetResourceAccountSettingResponse getResourceAccountSetting(GetResourceAccountSettingCommand cmd) {
        cmd.setPageSize(PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize()));
        CrossShardListingLocator locator = null ;
        if (cmd.getPageAnchor() != null){
            locator = new CrossShardListingLocator();
            locator.setAnchor(cmd.getPageAnchor());
        }
        List<Rentalv2PayAccount> accounts = this.rentalv2AccountProvider.listPayAccounts(UserContext.getCurrentNamespaceId(),
                cmd.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),cmd.getResourceTypeId(),
                RuleSourceType.RESOURCE.getCode(), null, locator, cmd.getPageSize()+1);
        if (accounts == null || accounts.size()==0)
            return null;
        GetResourceAccountSettingResponse response = new GetResourceAccountSettingResponse();
        if (accounts.size()>cmd.getPageSize()){
            accounts.remove(accounts.size()-1);
            response.setNextPageAnchor(accounts.get(accounts.size()-1).getId());
        }

        ListPayUsersByMerchantIdsCommand cmd2 = new ListPayUsersByMerchantIdsCommand();
        cmd2.setIds(accounts.stream().map(r -> r.getMerchantId()).collect(Collectors.toList()));
        ListPayUsersByMerchantIdsRestResponse restResponse = orderService.listPayUsersByMerchantIds(cmd2);
        List<PayUserDTO> payUserDTOS = restResponse.getResponse();
        response.setResourceAccounts(new ArrayList<>());
        accounts.forEach(r->{
            ResourceAccountDTO dto = new ResourceAccountDTO();
            dto.setId(r.getId());
            dto.setResourceName(r.getResourceName());
            dto.setResourceId(r.getSourceId());
            Optional<PayUserDTO> first = payUserDTOS.stream().filter(t -> t.getId().equals(r.getAccountId())).findFirst();
            if (first != null && first.isPresent()) {
                dto.setAccount(convertAccount(first.get()));
                response.getResourceAccounts().add(dto);
            }
        });
        return response;
    }

    @Override
    public void deleteResourceAccountSetting(Long id) {
        this.rentalv2AccountProvider.deletePayAccount(id,null,null,null);
    }

    @Override
    public void updateResourceAccountSetting(UpdateResourceAccountSettingCommand cmd) {
        List<Rentalv2PayAccount> accounts = rentalv2AccountProvider.listPayAccounts(UserContext.getCurrentNamespaceId(), cmd.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                null, RuleSourceType.DEFAULT.getCode(), cmd.getResourceTypeId(), null, null);
        if (accounts !=null && accounts.size()>0){//检测通用账户
            List<Rentalv2PayAccount> collect = accounts.stream().filter(r -> r.getMerchantId().equals(cmd.getAccountId())).collect(Collectors.toList());
            if (collect!=null && collect.size()>0)
                throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, 1001,
                        "此收款账户为通用收款账户，请重新选择");
        }
        if (cmd.getId() != null){//更新
            Rentalv2PayAccount account = rentalv2AccountProvider.getAccountById(cmd.getId());
            if (!account.getMerchantId().equals(cmd.getAccountId())){
                accounts = rentalv2AccountProvider.listPayAccounts(UserContext.getCurrentNamespaceId(), cmd.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                        null, RuleSourceType.RESOURCE.getCode(), cmd.getResourceId(), null, null);
                if (accounts!=null && accounts.size()>0)
                    throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, 1001,
                            cmd.getResourceName()+"已设置非通用收款账户，不可重复添加");
            }
            account.setMerchantId(cmd.getAccountId());//账号id换成商户Id
            account.setSourceId(cmd.getResourceId());
            account.setResourceName(cmd.getResourceName());
            rentalv2AccountProvider.updatePayAccount(account);
        }else {//新建
            accounts = rentalv2AccountProvider.listPayAccounts(UserContext.getCurrentNamespaceId(), cmd.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                    null, RuleSourceType.RESOURCE.getCode(), cmd.getResourceId(), null, null);
            if (accounts!=null && accounts.size()>0)
                throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, 1001,
                        cmd.getResourceName()+"已设置非通用收款账户，不可重复添加");
            Rentalv2PayAccount account = new Rentalv2PayAccount();
            account.setNamespaceId(UserContext.getCurrentNamespaceId());
            account.setCommunityId(cmd.getCommunityId());
            account.setResourceName(cmd.getResourceName());
            account.setResourceTypeId(cmd.getResourceTypeId());
            account.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
            account.setSourceType(RuleSourceType.RESOURCE.getCode());
            account.setSourceId(cmd.getResourceId());
            account.setMerchantId(cmd.getAccountId());//账号id换成商户Id
            rentalv2AccountProvider.createPayAccount(account);
        }
    }

    @Override
    public Long getRentalOrderPayeeAccount(Long rentalBillId) {
        RentalOrder order = rentalv2Provider.findRentalBillById(rentalBillId);
        RentalOrderHandler handler = rentalCommonService.getRentalOrderHandler(order.getResourceType());

        return handler.getAccountId(order);
    }

    private Long getRentalOrderMerchant(Long rentalBillId){
        RentalOrder order = rentalv2Provider.findRentalBillById(rentalBillId);
        RentalOrderHandler handler = rentalCommonService.getRentalOrderHandler(order.getResourceType());

        return handler.gerMerchantId(order);
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

    //检查买方（付款方）会员，无则创建
    @Deprecated
    public Long checkAndCreatePaymentUser(Long payerId, Integer namespaceId){
        PayUserDTO payUserDTO = new PayUserDTO();
        //根据tag查询支付帐号
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("checkAndCreatePaymentUser request={}", payerId);
        }
        List<PayUserDTO> payUserDTOs = payServiceV2.getPayUserList("EhUsers" + payerId.toString());
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("checkAndCreatePaymentUser response={}", payUserDTOs);
        }
        if(payUserDTOs == null || payUserDTOs.size() == 0){
            //创建个人账号
            payUserDTO = payServiceV2.createPersonalPayUserIfAbsent("EhUsers" + payerId.toString(), "NS"+namespaceId.toString());
            String defaultPhone = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"default.bind.phone", "");
            payServiceV2.bandPhone(payUserDTO.getId(), defaultPhone);

        }else {
            payUserDTO = payUserDTOs.get(0);
        }
        return payUserDTO.getId();
    }

    @Override
    public PreOrderDTO createPreOrder(PreOrderCommand cmd,RentalOrder order) {
        PreOrderDTO preOrderDTO = null;
        //1、查order表，如果order已经存在，则返回已有的合同，交易停止；否则，继续
        Rentalv2OrderRecord record = rentalv2AccountProvider.getOrderRecordByOrderNo(cmd.getOrderId());
        if (record != null){
            preOrderDTO = recordToDto(record, cmd);
            return preOrderDTO;
        }
        //2、检查买方（付款方）是否有会员，无则创建
//        Long payerAccount = this.checkAndCreatePaymentUser(UserContext.currentUserId(),UserContext.getCurrentNamespaceId());
//        if (payerAccount == null){
//            LOGGER.error("payerUserId no find, cmd={}", cmd);
//            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
//                    "payerUserId no find");
//        }else{
//            cmd.setPayerId(payerAccount);
//        }

        //3、收款方是否有会员，无则报错
        cmd.setBizPayeeId(this.getRentalOrderPayeeAccount(order.getId()));
        List<PayUserDTO> payUserDTOs = payServiceV2.listPayUsersByIds(Stream.of(cmd.getBizPayeeId()).collect(Collectors.toList()));
        if (payUserDTOs == null || payUserDTOs.size() == 0){
            LOGGER.error("payeeUserId no find, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, 1001,
                    "暂未绑定收款账户");
        }else
            cmd.setAccountName(payUserDTOs.get(0).getUserAliasName());

        //4、组装报文，发起下单请求
        PurchaseOrderCommandResponse orderCommandResponse = createOrder(cmd);

        //5、组装支付方式
        preOrderDTO = orderCommandResponseToDto(orderCommandResponse, cmd);

        //6、保存订单信息
        saveOrderRecord( cmd,orderCommandResponse);


        return preOrderDTO;
    }

    @Override
    public AddRentalBillItemV3Response createMerchantPreOrder(PreOrderCommand cmd,RentalOrder order) {
        AddRentalBillItemV3Response response = new AddRentalBillItemV3Response();
        //1、查order表，如果order已经存在，则返回已有的合同，交易停止；否则，继续
        Rentalv2OrderRecord record = rentalv2AccountProvider.getOrderRecordByOrderNo(cmd.getOrderId());
        if (record != null){
            response.setMerchantId(record.getMerchantId());
            response.setPayUrl(record.getPayUrl());
            response.setMerchantOrderId(record.getMerchantOrderId());
            return response;
        }
        //2、收款商户是否存在，无则报错
        cmd.setMerchantId(this.getRentalOrderMerchant(order.getId()));
        PayUserDTO payUserDTO = new PayUserDTO();
        if (cmd.getMerchantId() != null){
            GetPayAccountByMerchantIdCommand cmd2 = new GetPayAccountByMerchantIdCommand();
            cmd2.setId(cmd.getMerchantId());
            cmd2.setNamespaceId(UserContext.getCurrentNamespaceId());
            GetPayAccountByMerchantIdRestResponse payAccountByMerchantId = orderService.getPayAccountByMerchantId(cmd2);
            payUserDTO = payAccountByMerchantId.getResponse();
        }
        if (payUserDTO == null ){
            LOGGER.error("payeeUserId no find, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, 1001,
                    "暂未绑定收款账户");
        }else
            cmd.setAccountName(payUserDTO.getUserAliasName());
        //3、组装报文，发起下单请求
        CreateMerchantOrderResponse merchantOrderResp = createMerchantOrder(cmd, order);

        //4、组装并保存返回值
        response = saveMerchantOrderRecord(cmd,merchantOrderResp);


        return response;
    }

    private AddRentalBillItemV3Response saveMerchantOrderRecord(PreOrderCommand cmd,
                                                                CreateMerchantOrderResponse merchantOrderResp){
        AddRentalBillItemV3Response response = new AddRentalBillItemV3Response();
        response.setMerchantOrderId(merchantOrderResp.getMerchantOrderId());
        response.setMerchantId(merchantOrderResp.getMerchantId());
        response.setPayUrl(merchantOrderResp.getPayUrl());

        Rentalv2OrderRecord record = new Rentalv2OrderRecord();
        record.setOrderNo(cmd.getOrderId());
        record.setMerchantId(merchantOrderResp.getMerchantId());
        record.setAmount(changePayAmount(cmd.getAmount()));
        record.setAccountName(cmd.getAccountName());
        record.setPayUrl(merchantOrderResp.getPayUrl());
        record.setMerchantOrderId(merchantOrderResp.getMerchantOrderId());

        this.rentalv2AccountProvider.createOrderRecord(record);

        return response;
    }

    private CreateMerchantOrderResponse createMerchantOrder(PreOrderCommand preOrderCommand,RentalOrder order){
        CreatePurchaseOrderCommand createOrderCommand = preparePaymentBillOrder(preOrderCommand);
        CreateMerchantOrderCommand createMerchantOrderCommand = ConvertHelper.convert(createOrderCommand,CreateMerchantOrderCommand.class);
        createMerchantOrderCommand.setPaymentMerchantId(preOrderCommand.getMerchantId());
        RentalOrderHandler handler = rentalCommonService.getRentalOrderHandler(order.getResourceType());
        handler.processCreateMerchantOrderCmd(createMerchantOrderCommand,order);
        CreateMerchantOrderRestResponse createOrderResp = orderService.createMerchantOrder(createMerchantOrderCommand);
        if(!checkOrderRestResponseIsSuccess(createOrderResp)) {
            String scope = OrderErrorCode.SCOPE;
            int code = OrderErrorCode.ERROR_CREATE_ORDER_FAILED;
            String description = "Failed to create merchant order";
            if(createOrderResp != null) {
                code = (createOrderResp.getErrorCode() == null) ? code : createOrderResp.getErrorCode()  ;
                scope = (createOrderResp.getErrorScope() == null) ? scope : createOrderResp.getErrorScope();
                description = (createOrderResp.getErrorDescription() == null) ? description : createOrderResp.getErrorDescription();
            }
            throw RuntimeErrorException.errorWith(scope, code, description);
        }
        return createOrderResp.getResponse();
    }

    private PreOrderDTO orderCommandResponseToDto(PurchaseOrderCommandResponse orderCommandResponse, PreOrderCommand cmd){
        OrderCommandResponse response = orderCommandResponse.getPayResponse();
        PreOrderDTO dto = ConvertHelper.convert(response, PreOrderDTO.class);
        dto.setAmount(changePayAmount(cmd.getAmount()));
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

    @Override
    public void refundOrder(RentalOrder order,Long amount) {
        Rentalv2OrderRecord record = this.rentalv2AccountProvider.getOrderRecordByOrderNo(Long.valueOf(order.getOrderNo()));
        if (record == null)
            throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
                    RentalServiceErrorCode.ERROR_REFUND_ERROR,
                    "bill refund error");
        CreateRefundOrderCommand createRefundOrderCommand = new CreateRefundOrderCommand();
        String systemId = configurationProvider.getValue(0, PaymentConstants.KEY_SYSTEM_ID, "");
        createRefundOrderCommand.setBusinessSystemId(Long.parseLong(systemId));
        createRefundOrderCommand.setAccountCode("NS"+record.getNamespaceId().toString());
        if (record.getMerchantId() != null)
            createRefundOrderCommand.setMerchantOrderId(record.getMerchantOrderId());
        else //兼容旧的
             createRefundOrderCommand.setBusinessOrderNumber(record.getBizOrderNum());
        createRefundOrderCommand.setAmount(amount);
        createRefundOrderCommand.setBusinessOperatorType(BusinessPayerType.USER.getCode());
        createRefundOrderCommand.setBusinessOperatorId(String.valueOf(UserContext.currentUserId()));
        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
        String backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"refund.v2.callback.url.rental", "");
        String backUrl = homeUrl + contextPath + backUri;
        createRefundOrderCommand.setCallbackUrl(backUrl);
        createRefundOrderCommand.setSourceType(SourceType.MOBILE.getCode());

        CreateRefundOrderRestResponse refundOrderRestResponse = this.orderService.createRefundOrder(createRefundOrderCommand);
        if(refundOrderRestResponse != null && refundOrderRestResponse.getErrorCode() != null && refundOrderRestResponse.getErrorCode().equals(HttpStatus.OK.value())){

        } else{
            LOGGER.error("Refund failed from vendor, refundOrderNo={}, order={}, response={}", order.getOrderNo(), order,
                    refundOrderRestResponse);
            throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
                    RentalServiceErrorCode.ERROR_REFUND_ERROR,
                    "bill refund error");
        }

    }

    private void saveOrderRecord(PreOrderCommand cmd , PurchaseOrderCommandResponse orderCommandResponse) {
        OrderCommandResponse response = orderCommandResponse.getPayResponse();
        Rentalv2OrderRecord record = new Rentalv2OrderRecord();
        record.setOrderNo(cmd.getOrderId());
        record.setBizOrderNum(response.getBizOrderNum());
        record.setPayOrderId(response.getOrderId());
        record.setAccountId(cmd.getBizPayeeId());
        record.setAmount(changePayAmount(cmd.getAmount()));
        record.setOrderCommitNonce(response.getOrderCommitNonce());
        record.setOrderCommitTimestamp(response.getOrderCommitTimestamp());
        record.setOrderCommitToken(response.getOrderCommitToken());
        record.setOrderCommitUrl(response.getOrderCommitUrl());
        record.setPayInfo(response.getPayInfo());
        record.setAccountName(cmd.getAccountName());


        this.rentalv2AccountProvider.createOrderRecord(record);
    }

    private PurchaseOrderCommandResponse createOrder(PreOrderCommand preOrderCommand){
        //组装参数
//        CreateOrderCommand createOrderCommand = newCreateOrderCommandForPay(preOrderCommand);
//
//
//        //向支付预下单
//        if(LOGGER.isDebugEnabled()) {LOGGER.debug("createOrderPayV2-command=" + GsonUtil.toJson(createOrderCommand));}
//        CreateOrderRestResponse createOrderResp = payServiceV2.createCustomOrder(createOrderCommand);
//        if(LOGGER.isDebugEnabled()) {LOGGER.debug("createOrderPayV2-response=" + GsonUtil.toJson(createOrderResp));}
//        if(!createOrderResp.getErrorCode().equals(200)) {
//            LOGGER.error("create order fail");
//            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
//                    "create order fail");
//        }
//
//        OrderCommandResponse  response = createOrderResp.getResponse();

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

    /*
     * 由于从支付系统里回来的CreateOrderRestResponse有可能没有errorScope，故不能直接使用CreateOrderRestResponse.isSuccess()来判断，
       CreateOrderRestResponse.isSuccess()里会对errorScope进行比较
     */
    private boolean checkOrderRestResponseIsSuccess(RestResponseBase response){
        if(response != null && response.getErrorCode() != null
                && (response.getErrorCode().intValue() == 200 || response.getErrorCode().intValue() == 201))
            return true;
        return false;
    }

    private CreatePurchaseOrderCommand preparePaymentBillOrder(PreOrderCommand cmd) {
        CreatePurchaseOrderCommand preOrderCommand = new CreatePurchaseOrderCommand();
        //PaymentParamsDTO转为Map
        Map<String, String> flattenMap = new HashMap<>();
        StringHelper.toStringMap(null, cmd.getPaymentParams(), flattenMap);

        BigDecimal totalAmountCents = cmd.getAmount();
        preOrderCommand.setAmount(changePayAmount(totalAmountCents));


        String accountCode = "NS"+cmd.getNamespaceId();

        preOrderCommand.setAccountCode(accountCode);
        preOrderCommand.setClientAppName(cmd.getClientAppName());

        preOrderCommand.setBusinessOrderType(cmd.getOrderType());
        // 移到统一订单系统完成
        // String BizOrderNum  = getOrderNum(orderId, OrderType.OrderTypeEnum.WUYE_CODE.getPycode());
        // preOrderCommand.setBizOrderNum(BizOrderNum);
        BusinessPayerType payerType = BusinessPayerType.USER;
        preOrderCommand.setBusinessPayerType(payerType.getCode());
        preOrderCommand.setBusinessPayerId(String.valueOf(UserContext.currentUserId()));
        String businessPayerParams = getBusinessPayerParams(cmd);
        preOrderCommand.setBusinessPayerParams(businessPayerParams);

       // preOrderCommand.setPaymentPayeeType(billGroup.getBizPayeeType()); 不填会不会有问题?
        preOrderCommand.setPaymentPayeeId(cmd.getBizPayeeId());

        preOrderCommand.setExtendInfo(cmd.getExtendInfo());
        preOrderCommand.setPaymentParams(flattenMap);
        preOrderCommand.setPaymentType(cmd.getPaymentType());
        preOrderCommand.setCommitFlag(cmd.getCommitFlag());
        //preOrderCommand.setExpirationMillis(EXPIRE_TIME_15_MIN_IN_SEC);
        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
        String backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.callback.url.rental", "");
        String backUrl = homeUrl + contextPath + backUri;
        preOrderCommand.setCallbackUrl(backUrl);
        preOrderCommand.setExtendInfo(cmd.getExtendInfo());
        preOrderCommand.setGoodsName("资源预订");
        preOrderCommand.setGoodsDescription(null);
        preOrderCommand.setIndustryName(null);
        preOrderCommand.setIndustryCode(null);
        preOrderCommand.setSourceType(SourceType.MOBILE.getCode());
        preOrderCommand.setOrderRemark1("资源预订");
        //preOrderCommand.setOrderRemark2(String.valueOf(cmd.getOrderId()));
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

    private CreateOrderCommand newCreateOrderCommandForPay(PreOrderCommand cmd){
        //PaymentParamsDTO转为Map
        Map<String, String> flattenMap = new HashMap<>();
        StringHelper.toStringMap(null, cmd.getPaymentParams(), flattenMap);

        CreateOrderCommand createOrderCmd = new CreateOrderCommand();
        //业务系统中的订单号，请在整个业务系统中约定好唯一规则；
        createOrderCmd.setOrderType(com.everhomes.pay.order.OrderType.PURCHACE.getCode());
        createOrderCmd.setAmount(changePayAmount(cmd.getAmount()));//需要将元转化为分，使用此类: AmountUtil.unitToCent()
        String BizOrderNum  = getOrderNum(cmd.getOrderId(),cmd.getOrderType());
        LOGGER.info("BizOrderNum is = {} "+BizOrderNum);
        createOrderCmd.setBizOrderNum(BizOrderNum);
        createOrderCmd.setClientAppName(cmd.getClientAppName());
        createOrderCmd.setPayerUserId(cmd.getPayerId());//付款方ID
        createOrderCmd.setPayeeUserId(cmd.getBizPayeeId());//收款方ID
        createOrderCmd.setPaymentParams(flattenMap);
        createOrderCmd.setPaymentType(cmd.getPaymentType());
        if(cmd.getExpiration() != null) {
            createOrderCmd.setExpirationMillis(cmd.getExpiration());
        }
        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
        String backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.callback.url.rental", "");
        String backUrl = homeUrl + contextPath + backUri;
        createOrderCmd.setBackUrl(backUrl);
        createOrderCmd.setExtendInfo(cmd.getExtendInfo());
        createOrderCmd.setGoodsName("资源预订");
        createOrderCmd.setGoodsDescription(null);
        createOrderCmd.setIndustryCode(null);
        createOrderCmd.setIndustryName(null);
        createOrderCmd.setSourceType(SourceType.MOBILE.getCode());
        createOrderCmd.setOrderRemark1("资源预订");
        createOrderCmd.setOrderRemark2(String.valueOf(cmd.getOrderId()));
        createOrderCmd.setOrderRemark3(String.valueOf(cmd.getCommunityId()));
        createOrderCmd.setOrderRemark4(null);
        createOrderCmd.setOrderRemark5(null);
        if(UserContext.getCurrentNamespaceId() != null) {
            createOrderCmd.setAccountCode("NS"+UserContext.getCurrentNamespaceId());
        }
        createOrderCmd.setCommitFlag(0);
        if(cmd.getCommitFlag() != null){
            createOrderCmd.setCommitFlag(cmd.getCommitFlag());
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

    private PreOrderDTO recordToDto(Rentalv2OrderRecord record, PreOrderCommand cmd){
        PreOrderDTO dto = ConvertHelper.convert(record, PreOrderDTO.class);
        dto.setAmount(changePayAmount(cmd.getAmount()));
        dto.setExtendInfo(cmd.getExtendInfo());
        if (cmd.getClientAppName() != null) {
            ListClientSupportPayMethodCommandResponse response = payServiceV2.listClientSupportPayMethod("NS" + UserContext.getCurrentNamespaceId(),
                    cmd.getClientAppName());
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
        }
        return dto;
    }

    public static boolean verifyCallbackSignature(MerchantPaymentNotificationCommand cmd) {
        Map<String, String> params = new HashMap();
        StringHelper.toStringMap(null, cmd, params);
        params.remove("signature");
        LOGGER.info("the cmd is {}  the pre signature is {} the after signature is {}",cmd,cmd.getSignature(),SignatureHelper.computeSignature(params,PaySettings.getSecretKey()));
        return SignatureHelper.verifySignature(params, PaySettings.getSecretKey(), cmd.getSignature());
    }

    @Override
    public void payNotify(MerchantPaymentNotificationCommand cmd) {
        if (!verifyCallbackSignature(cmd)) {
            if (configurationProvider.getBooleanValue("check.signature.flag",true))
                 throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
                    "signature wrong");
        }
        if(cmd.getPaymentStatus() == null) {
            LOGGER.info(" ----------------- - - - PAY FAIL command is "+cmd.toString());
        }

        //success
        if (cmd.getPaymentStatus() != null) {
            Rentalv2OrderRecord record;
            BigDecimal couponAmount = new BigDecimal(0);
            if (cmd.getMerchantOrderId() != null) {
                record = rentalv2AccountProvider.getOrderRecordByMerchantOrderId(cmd.getMerchantOrderId());
                record.setBizOrderNum(cmd.getBizOrderNum()); //存下来 开发票的时候使用
                rentalv2AccountProvider.updateOrderRecord(record);
                couponAmount = changePayAmount(cmd.getCouponAmount());
            } else
                record = rentalv2AccountProvider.getOrderRecordByBizOrderNo(cmd.getBizOrderNum());
            RentalOrder order = rentalProvider.findRentalBillByOrderNo(record.getOrderNo().toString());
            order.setPaidMoney(order.getPaidMoney().add(changePayAmount(cmd.getAmount())));
            order.setAccountName(record.getAccountName()); //记录收款方账号
            if (cmd.getPaymentType() != null)
                switch (cmd.getPaymentType()) {
                    case 1:
                    case 7:
                    case 9:
                    case 21:
                        order.setVendorType(VendorType.WEI_XIN.getCode());
                        order.setPayChannel(PayChannel.NORMAL_PAY.getCode());
                        break;
                    case 29:
                        order.setPayChannel(PayChannel.ENTERPRISE_PAY_CHARGE.getCode());
                        break;
                    default:
                        order.setVendorType(VendorType.ZHI_FU_BAO.getCode());
                        order.setPayChannel(PayChannel.NORMAL_PAY.getCode());
                        break;
                }

            order.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            order.setPayTime(new Timestamp(DateHelper.currentGMTTime().getTime()));


            if (order.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode()) ||
                    order.getStatus().equals(SiteBillStatus.APPROVING.getCode())) {
                //判断支付金额与订单金额是否相同
                if (order.getPayTotalMoney().compareTo(order.getPaidMoney().add(couponAmount)) == 0) {
                    onOrderRecordSuccess(order);
                    onOrderSuccess(order);
                } else {
                    LOGGER.error("待付款订单:id [" + order.getId() + "]付款金额有问题： 应该付款金额：" + order.getPayTotalMoney() + "实际付款金额：" + order.getPaidMoney());
                }
            } else if (order.getStatus().equals(SiteBillStatus.SUCCESS.getCode())) {
                LOGGER.error("待付款订单:id [" + order.getId() + "] 状态已经是成功预约");
            } else if (order.getStatus().equals(SiteBillStatus.IN_USING.getCode()) || (order.getStatus().equals(SiteBillStatus.OWING_FEE.getCode()))) {//vip停车的欠费和续费
                if (order.getPayTotalMoney().compareTo(order.getPaidMoney()) == 0) {
                    if (order.getStatus().equals(SiteBillStatus.OWING_FEE.getCode())) {
                        order.setStatus(SiteBillStatus.COMPLETE.getCode());
                    } else
                        rentalService.renewOrderSuccess(order, order.getRentalCount());
                    rentalProvider.updateRentalBill(order);
                    onOrderRecordSuccess(order);
                } else {
                    LOGGER.error("待付款订单:id [" + order.getId() + "]付款金额有问题： 应该付款金额：" + order.getPayTotalMoney() + "实际付款金额：" + order.getPaidMoney());
                }
            } else
                LOGGER.error("待付款订单:id [" + order.getId() + "]状态有问题： 订单状态是：" + order.getStatus());

        }
    }

    public BigDecimal changePayAmount(Long amount){

        if(amount == null){
            return new BigDecimal(0);
        }
        return  new BigDecimal(amount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
    }

    private void onOrderRecordSuccess(RentalOrder order){
        Rentalv2OrderRecord record = this.rentalv2AccountProvider.getOrderRecordByOrderNo(Long.valueOf(order.getOrderNo()));
        if (record != null){
            record.setStatus((byte)1);
            this.rentalv2AccountProvider.updateOrderRecord(record);
        }
    }

    public void onOrderSuccess(RentalOrder order){
        if (order.getPayMode().equals(PayMode.ONLINE_PAY.getCode())) {
            //支付成功之后创建工作流
            order.setStatus(SiteBillStatus.SUCCESS.getCode());
            rentalProvider.updateRentalBill(order);
            rentalService.onOrderSuccess(order);
        } else {
            rentalProvider.updateRentalBill(order);
            //改变订单状态
            rentalService.changeRentalOrderStatus(order,SiteBillStatus.SUCCESS.getCode(),true);
            //工作流自动进到下一节点
            FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(order.getId(), REFER_TYPE, moduleId);
            FlowCaseTree tree = flowService.getProcessingFlowCaseTree(flowCase.getId());
            flowCase = tree.getLeafNodes().get(0).getFlowCase();//获取真正正在进行的flowcase
            FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
            stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
            stepDTO.setFlowCaseId(flowCase.getId());
            stepDTO.setFlowMainId(flowCase.getFlowMainId());
            stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
            stepDTO.setFlowVersion(flowCase.getFlowVersion());
            stepDTO.setStepCount(flowCase.getStepCount());
            flowService.processAutoStep(stepDTO);
        }
    }

    @Override
    public void refundNotify(OrderPaymentNotificationCommand cmd) {
        if (!PayUtil.verifyCallbackSignature(cmd))
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
                    "signature wrong");

            Rentalv2OrderRecord record = rentalv2AccountProvider.getOrderRecordByBizOrderNo(cmd.getBizOrderNum());
            RentalRefundOrder rentalRefundOrder = this.rentalProvider.getRentalRefundOrderByOrderNo(record.getOrderNo().toString());
            RentalOrder bill = this.rentalProvider.findRentalBillById(rentalRefundOrder.getOrderId());
            rentalRefundOrder.setStatus(SiteBillStatus.REFUNDED.getCode());
            bill.setStatus(SiteBillStatus.REFUNDED.getCode());
            rentalProvider.updateRentalBill(bill);
            rentalProvider.updateRentalRefundOrder(rentalRefundOrder);
            RentalMessageHandler messageHandler = rentalCommonService.getRentalMessageHandler(bill.getResourceType());
            messageHandler.refundOrderSuccessSendMessage(bill);

    }

    public Long changePayAmount(BigDecimal amount){

        if(amount == null){
            return 0L;
        }
        return  amount.multiply(new BigDecimal(100)).longValue();
    }

}
