package com.everhomes.payment;

import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.payment.GetAccountSettingCommand;
import com.everhomes.rest.payment.UpdateAccountSettingCommand;
import com.everhomes.rest.order.PreOrderCommand;

import java.util.List;

public interface PaymentCardPayService {

    List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd);

    ListBizPayeeAccountDTO getAccountSetting(GetAccountSettingCommand cmd);

    void updateAccountSetting(UpdateAccountSettingCommand cmd);

    PreOrderDTO createPreOrder(PreOrderCommand cmd, PaymentCardRechargeOrder order);

    void refundOrder(PaymentCardRechargeOrder order);

}
