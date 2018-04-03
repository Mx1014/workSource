//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>billGroupRuleId:账单组收费项目id</li>
 *</ul>
 */
public class BillGroupRuleIdCommand {
    private Long billGroupRuleId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillGroupRuleId() {
        return billGroupRuleId;
    }

    public void setBillGroupRuleId(Long billGroupRuleId) {
        this.billGroupRuleId = billGroupRuleId;
    }

    public BillGroupRuleIdCommand() {

    }
}
