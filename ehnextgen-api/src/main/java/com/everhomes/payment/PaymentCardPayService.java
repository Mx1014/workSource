package com.everhomes.payment;

import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.payment.GetAccountSettingCommand;
import com.everhomes.rest.payment.UpdateAccountSettingCommand;

import java.util.List;

public interface PaymentCardPayService {

    List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd);

    ListBizPayeeAccountDTO getAccountSetting(GetAccountSettingCommand cmd);

    void updateAccountSetting(UpdateAccountSettingCommand cmd);

}
