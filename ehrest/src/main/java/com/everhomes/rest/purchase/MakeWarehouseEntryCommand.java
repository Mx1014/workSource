//@formatter:off
package com.everhomes.rest.purchase;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2018/1/10.
 */
/**
 *<ul>
 * <li>purchaseRequestId:采购单id</li>
 *</ul>
 */
public class MakeWarehouseEntryCommand {
    private Long purchaseRequestId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getPurchaseRequestId() {
        return purchaseRequestId;
    }

    public void setPurchaseRequestId(Long purchaseRequestId) {
        this.purchaseRequestId = purchaseRequestId;
    }
}
