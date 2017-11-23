//@formatter:off
package com.everhomes.asset;

import com.everhomes.rest.asset.ListPaymentBillCmd;
import com.everhomes.rest.asset.ListPaymentBillResp;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public interface RemoteAccessService {
    ListPaymentBillResp listOrderPayment(ListPaymentBillCmd cmd) throws Exception;
}
