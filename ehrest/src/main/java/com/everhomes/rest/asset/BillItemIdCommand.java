//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>billItemId:账单收费项id</li>
 *</ul>
 */
public class BillItemIdCommand {
    private Long billItemId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillItemId() {
        return billItemId;
    }

    public void setBillItemId(Long billItemId) {
        billItemId = billItemId;
    }

    public BillItemIdCommand() {

    }
}
