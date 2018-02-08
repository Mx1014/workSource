package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>approvalId: 审批id</li>
 * <li>formOriginId: 表单id</li>
 * </ul>
 */
public class SetGeneralApprovalFormCommand {

    private Long approvalId;

    private Long formOriginId;

    public SetGeneralApprovalFormCommand() {
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
