//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>exemptionItemId:账单减免项id</li>
 *</ul>
 */
public class ExemptionItemIdCommand {
    private Long exemptionItemId;

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
