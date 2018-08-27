package com.everhomes.payment;

import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.order.PaymentUserStatus;
import com.everhomes.rest.payment.GetAccountSettingCommand;
import com.everhomes.rest.payment.UpdateAccountSettingCommand;
import com.everhomes.paySDK.PayUtil;
import com.everhomes.paySDK.pojo.PayUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.everhomes.organization.pm.pay.GsonUtil;

import java.util.ArrayList;
import java.util.List;

public class PaymentCardPayServiceImpl implements  PaymentCardPayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardPayServiceImpl.class);

    @Autowired
    private com.everhomes.paySDK.api.PayService payServiceV2;

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
        return null;
    }

    @Override
    public void updateAccountSetting(UpdateAccountSettingCommand cmd) {

    }
}
