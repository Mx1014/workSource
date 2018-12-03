package com.everhomes.rentalv2;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.promotion.order.CreateMerchantOrderResponse;
import com.everhomes.rest.promotion.order.MerchantPaymentNotificationCommand;
import com.everhomes.rest.rentalv2.AddRentalBillItemV3Response;
import com.everhomes.rest.rentalv2.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.rentalv2.admin.*;

import java.util.List;

public interface Rentalv2PayService {

    List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd);

    ListBizPayeeAccountDTO getGeneralAccountSetting(GetGeneralAccountSettingCommand cmd);

    void updateGeneralAccountSetting(UpdateGeneralAccountSettingCommand cmd);

    GetResourceAccountSettingResponse getResourceAccountSetting(GetResourceAccountSettingCommand cmd);

    void deleteResourceAccountSetting(Long id);

    void updateResourceAccountSetting(UpdateResourceAccountSettingCommand cmd);

    Long getRentalOrderPayeeAccount(Long rentalBillId);

    Long checkAndCreatePaymentUser(Long payerId, Integer namespaceId);

    PreOrderDTO createPreOrder(PreOrderCommand cmd,RentalOrder order);

    AddRentalBillItemV3Response createMerchantPreOrder(PreOrderCommand cmd,RentalOrder order);

    void refundOrder (RentalOrder order,Long amount);

    void payNotify(MerchantPaymentNotificationCommand cmd);

    void refundNotify(MerchantPaymentNotificationCommand cmd);
}
