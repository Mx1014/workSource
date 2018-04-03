//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>subjectBillGroupId:需要调整位置的账单组id</li>
 * <li>targetBillGroupId:被互换位置的账单组id</li>
 *</ul>
 */
public class AdjustBillGroupOrderCommand {
    private Long subjectBillGroupId;
    private Long targetBillGroupId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getSubjectBillGroupId() {
        return subjectBillGroupId;
    }

    public void setSubjectBillGroupId(Long subjectBillGroupId) {
        this.subjectBillGroupId = subjectBillGroupId;
    }

    public Long getTargetBillGroupId() {
        return targetBillGroupId;
    }

    public void setTargetBillGroupId(Long targetBillGroupId) {
        this.targetBillGroupId = targetBillGroupId;
    }

    public AdjustBillGroupOrderCommand() {

    }
}
