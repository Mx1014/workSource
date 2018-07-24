//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>billItemId:账单收费项id</li>
 * <li>billGroupId:账单组id</li>
 *</ul>
 */
public class BillItemIdCommand {
    private Long billItemId;
    private Long billGroupId;

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public Long getBillItemId() {
        return billItemId;
    }

    public void setBillItemId(Long billItemId) {
        this.billItemId = billItemId;
    }
}
