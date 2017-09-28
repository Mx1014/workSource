//@formatter:off
package com.everhomes.asset;

import com.everhomes.rest.asset.ListPaymentBillCmd;
import com.everhomes.rest.asset.ListPaymentBillResp;
import com.everhomes.rest.asset.PaymentAccountResp;
import com.everhomes.rest.user.UserInfo;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public interface PaymentService {
    ListPaymentBillResp listPaymentBill(ListPaymentBillCmd cmd, UserInfo user) throws Exception;

    PaymentAccountResp findPaymentAccount();
}
