//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>billGroupId:账单组id</li>
 *</ul>
 */
public class BillGroupIdCommand {
    private Long billGroupId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public BillGroupIdCommand() {
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }
}
