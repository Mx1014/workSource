//@formatter:off
package com.everhomes.asset;

import com.everhomes.rest.asset.ListPaymentBillCmdForEnt;
import com.everhomes.rest.asset.ListPaymentBillRespForEnt;

/**
 * @author created by yangcx
 * @date 2018年5月23日----下午1:41:39
 */
public interface RemoteForEntAccessService {
    ListPaymentBillRespForEnt listOrderPaymentForEnt(ListPaymentBillCmdForEnt cmd) throws Exception;
}
