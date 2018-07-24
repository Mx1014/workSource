//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>exemptionItemId:账单减免项id</li>
 * <li>billGroupId:账单组id</li>
 *</ul>
 */
public class ExemptionItemIdCommand {
    private Long exemptionItemId;
    private Long billGroupId;

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getExemptionItemId() {
        return exemptionItemId;
    }

    public void setExemptionItemId(Long exemptionItemId) {
        this.exemptionItemId = exemptionItemId;
    }

    public ExemptionItemIdCommand() {

    }
}
