package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>detailId: 人员档案id</li>
 * <li>remarks: 离职备注</li>
 * </ul>
 */
public class LeaveTheJobCommand {

    @NotNull
    private Long organizationId;

    @NotNull
    private Long detailId;

    private String remarks;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
