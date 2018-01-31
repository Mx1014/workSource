package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>参数:
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>organizationId: 总公司id</li>
 * <li>month: 月份</li>
 * <li>exportToken: 导出token</li>
 * </ul>
 */
public class ExportSalaryReportCommand {

    private String ownerType;

    private Long ownerId;

    private Long organizationId;

    private String month;

    private String exportToken;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getExportToken() {
        return exportToken;
    }

    public void setExportToken(String exportToken) {
        this.exportToken = exportToken;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
}
