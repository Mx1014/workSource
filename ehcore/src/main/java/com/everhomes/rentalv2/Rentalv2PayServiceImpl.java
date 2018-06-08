package com.everhomes.rentalv2;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.order.PaymentServiceConfig;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.pay.order.CreateOrderCommand;
import com.everhomes.pay.order.OrderCommandResponse;
import com.everhomes.pay.order.SourceType;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.order.*;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.RuleSourceType;
import com.everhomes.rest.rentalv2.admin.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Rentalv2PayServiceImpl implements Rentalv2PayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Rentalv2PayServiceImpl.class);

    @Autowired
    private com.everhomes.paySDK.api.PayService payServiceV2;
    @Autowired
    private Rentalv2AccountProvider rentalv2AccountProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Value("${server.contextPath:}")
    private String contextPath;

    @Override
    public List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd) {
        String userPrefix = "EhBizBusinesses";
        List<PayUserDTO> payUserDTOs = payServiceV2.getPayUserList(userPrefix + cmd.getOrganizationId(), cmd.getCommunityId().toString());
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

    @Override
    public ListBizPayeeAccountDTO getGeneralAccountSetting(GetGeneralAccountSettingCommand cmd) {
        List<Rentalv2PayAccount> accounts = this.rentalv2AccountProvider.listPayAccounts(UserContext.getCurrentNamespaceId(), cmd.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                null,RuleSourceType.DEFAULT.getCode(), cmd.getResourceTypeId(), null, null);
        if (accounts == null || accounts.size() == 0)
            return null;
        List<PayUserDTO> payUserDTOs = payServiceV2.listPayUsersByIds(accounts.stream().map(r -> r.getAccountId()).collect(Collectors.toList()));
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
        account.setAccountId(cmd.getAccountId());
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
        List<PayUserDTO> payUserDTOS = payServiceV2.listPayUsersByIds(accounts.stream().map(r -> r.getAccountId()).collect(Collectors.toList()));
        response.setResourceAccounts(new ArrayList<>());
        accounts.forEach(r->{
            ResourceAccountDTO dto = new ResourceAccountDTO();
            dto.setId(r.getId());
            dto.setResourceName(r.getResourceName());
            dto.setResourceId(r.getSourceId());
            PayUserDTO payUserDTO = payUserDTOS.stream().filter(t -> t.getId().equals(r.getAccountId())).findFirst().get();
            dto.setAccount(convertAccount(payUserDTO));
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
            List<Rentalv2PayAccount> collect = accounts.stream().filter(r -> r.getAccountId().equals(cmd.getAccountId())).collect(Collectors.toList());
            if (collect!=null && collect.size()>0)
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "此收款账户为通用收款账户，请重新选择");
        }
        if (cmd.getId() != null){//更新
            Rentalv2PayAccount account = rentalv2AccountProvider.getAccountById(cmd.getId());
            if (!account.getAccountId().equals(cmd.getAccountId())){
                accounts = rentalv2AccountProvider.listPayAccounts(UserContext.getCurrentNamespaceId(), cmd.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                        null, RuleSourceType.RESOURCE.getCode(), cmd.getResourceId(), null, null);
                if (accounts!=null && accounts.size()>0)
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            cmd.getResourceName()+"已设置非通用收款账户，不可重复添加");
            }
            account.setAccountId(cmd.getAccountId());
            account.setSourceId(cmd.getResourceId());
            account.setResourceName(cmd.getResourceName());
            rentalv2AccountProvider.updatePayAccount(account);
        }else {//新建
            accounts = rentalv2AccountProvider.listPayAccounts(UserContext.getCurrentNamespaceId(), cmd.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                    null, RuleSourceType.RESOURCE.getCode(), cmd.getResourceId(), null, null);
            if (accounts!=null && accounts.size()>0)
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        cmd.getResourceName()+"已设置非通用收款账户，不可重复添加");
            Rentalv2PayAccount account = new Rentalv2PayAccount();
            account.setNamespaceId(UserContext.getCurrentNamespaceId());
            account.setCommunityId(cmd.getCommunityId());
            account.setResourceName(cmd.getResourceName());
            account.setResourceTypeId(cmd.getResourceTypeId());
            account.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
            account.setSourceType(RuleSourceType.RESOURCE.getCode());
            account.setSourceId(cmd.getResourceId());
            account.setAccountId(cmd.getAccountId());
            rentalv2AccountProvider.createPayAccount(account);
        }
    }

    @Override
    public Long getRentalOrderPayeeAccount(Long rentalBillId) {
        RentalOrder order = rentalv2Provider.findRentalBillById(rentalBillId);
        //查特殊账户
        List<Rentalv2PayAccount> accounts = this.rentalv2AccountProvider.listPayAccounts(null, order.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                null, RuleSourceType.RESOURCE.getCode(), order.getRentalResourceId(), null, null);
        if (accounts != null && accounts.size()>0)
            return accounts.get(0).getAccountId();
        //查通用账户
        accounts = this.rentalv2AccountProvider.listPayAccounts(null, order.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                null, RuleSourceType.DEFAULT.getCode(), order.getResourceTypeId(), null, null);
        if (accounts != null && accounts.size()>0)
            return accounts.get(0).getAccountId();
        return null;
    }

    private ListBizPayeeAccountDTO convertAccount(PayUserDTO payUserDTO){
        if (payUserDTO == null)
            return null;
        ListBizPayeeAccountDTO dto = new ListBizPayeeAccountDTO();
        // 支付系统中的用户ID
        dto.setAccountId(payUserDTO.getId());
        // 用户向支付系统注册帐号时填写的帐号名称
        dto.setAccountName(payUserDTO.getRemark());
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
            payUserDTO = payServiceV2.createPersonalPayUserIfAbsent("EhUsers" + payerId.toString(), namespaceId.toString());
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
        Long payerAccount = this.checkAndCreatePaymentUser(UserContext.currentUserId(),UserContext.getCurrentNamespaceId());
        if (payerAccount == null){
            LOGGER.error("payerUserId no find, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
                    "payerUserId no find");
        }else{
            cmd.setPayerId(payerAccount);
        }

        //3、收款方是否有会员，无则报错
        cmd.setBizPayeeId(this.getRentalOrderPayeeAccount(order.getId()));
        List<PayUserDTO> payUserDTOs = payServiceV2.listPayUsersByIds(Stream.of(cmd.getBizPayeeId()).collect(Collectors.toList()));
        if (payUserDTOs == null || payUserDTOs.size() == 0){
            LOGGER.error("payeeUserId no find, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "暂未绑定收款账户");
        }

        //4、组装报文，发起下单请求
        OrderCommandResponse orderCommandResponse = createOrder(cmd);

        //5、组装支付方式
        preOrderDTO = orderCommandResponseToDto(orderCommandResponse, cmd);

        //6、保存订单信息
        saveOrderRecord( cmd.getOrderId(),orderCommandResponse, cmd.getBizPayeeId());


        return preOrderDTO;
    }

    private void saveOrderRecord(Long orderNo, OrderCommandResponse orderCommandResponse, Long bizPayeeId) {
        Rentalv2OrderRecord record = new Rentalv2OrderRecord();
        record.setOrderNo(orderNo);
        record.setAccountId(bizPayeeId);
        record.setOrderCommitNonce(orderCommandResponse.getOrderCommitNonce());
        record.setOrderCommitTimestamp(orderCommandResponse.getOrderCommitTimestamp());
        record.setOrderCommitToken(orderCommandResponse.getOrderCommitToken());
        record.setOrderCommitUrl(orderCommandResponse.getOrderCommitUrl());
        record.setPayInfo(orderCommandResponse.getPayInfo());

        this.rentalv2AccountProvider.createOrderRecord(record);
    }

    private OrderCommandResponse createOrder(PreOrderCommand preOrderCommand){
        //组装参数
        CreateOrderCommand createOrderCommand = newCreateOrderCommandForPay(preOrderCommand);
        //向支付预下单
        if(LOGGER.isDebugEnabled()) {LOGGER.debug("createOrderPayV2-command=" + GsonUtil.toJson(createOrderCommand));}
        CreateOrderRestResponse createOrderResp = payServiceV2.createCustomOrder(createOrderCommand);
        if(LOGGER.isDebugEnabled()) {LOGGER.debug("createOrderPayV2-response=" + GsonUtil.toJson(createOrderResp));}
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
        createOrderCmd.setOrderType(com.everhomes.pay.order.OrderType.PURCHACE.getCode());
        createOrderCmd.setAmount(cmd.getAmount());//需要将元转化为分，使用此类: AmountUtil.unitToCent()
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
        String backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"asset.pay.v2.callback.url", "");
        String backUrl = homeUrl + contextPath + backUri;
        createOrderCmd.setBackUrl(backUrl);
        createOrderCmd.setExtendInfo(cmd.getExtendInfo());
        createOrderCmd.setGoodsName("资源预约");
        createOrderCmd.setGoodsDescription(null);
        createOrderCmd.setIndustryCode(null);
        createOrderCmd.setIndustryName(null);
        createOrderCmd.setSourceType(SourceType.MOBILE.getCode());
        createOrderCmd.setOrderRemark1(String.valueOf(cmd.getOrderType()));
        createOrderCmd.setOrderRemark2(String.valueOf(cmd.getOrderId()));
        createOrderCmd.setOrderRemark3(String.valueOf(cmd.getCommunityId()));
        createOrderCmd.setOrderRemark4(null);
        createOrderCmd.setOrderRemark5(null);
        if(UserContext.getCurrentNamespaceId() != null) {
            createOrderCmd.setAccountCode(UserContext.getCurrentNamespaceId().toString());
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
        dto.setAmount(cmd.getAmount());
        dto.setExtendInfo(cmd.getExtendInfo());

        //TODO 支付方式组装
        //dto.setPayMethod(payMethods);
        return dto;
    }
}
