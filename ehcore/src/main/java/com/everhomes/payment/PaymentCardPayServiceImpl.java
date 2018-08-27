package com.everhomes.payment;

import com.everhomes.entity.EntityType;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.order.PaymentUserStatus;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.payment.GetAccountSettingCommand;
import com.everhomes.rest.payment.UpdateAccountSettingCommand;
import com.everhomes.paySDK.PayUtil;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.rentalv2.PreOrderCommand;
import com.everhomes.rest.rentalv2.RentalServiceErrorCode;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.everhomes.organization.pm.pay.GsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaymentCardPayServiceImpl implements  PaymentCardPayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardPayServiceImpl.class);

    @Autowired
    private com.everhomes.paySDK.api.PayService payServiceV2;
    @Autowired
    private PaymentCardProvider paymentCardProvider;

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
    public PreOrderDTO createPreOrder(PreOrderCommand cmd) {
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

        return null;
    }

    Long getPayeeAccountByCommunityId(Long communityId){
        List<PaymentCardAccount> accounts = paymentCardProvider.listPaymentCardAccounts(EntityType.COMMUNITY.getCode(),communityId);
        if (accounts != null && accounts.size() > 0)
            return accounts.get(0).getAccountId();
        return null;
    }
}
