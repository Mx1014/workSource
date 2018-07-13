package com.everhomes.rest.invitation_card;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>sourcId: 需要更新启用表单的审批项id</li>
 *     <li>formOriginId: 启用的表单formOriginId</li>
 *     <li>formVersion: 启用的表单的formVersion</li>
 * </ul>
 */
public class updateInvitationApprovalActiveFormCommond {
    private Long sourcId;
    private Long formOriginId;
    private Long formVersion;

    public Long getSourcId() {
        return sourcId;
    }

    public void setSourcId(Long sourcId) {
        this.sourcId = sourcId;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
