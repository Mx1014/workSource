package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>organizationId: 公司id</li>
 * <li>detailId: 员工id</li>
 * <li>isExport: 0-no 1-yes</li>
 * </ul>
 */
public class GetArchivesEmployeeCommand {

    private Integer namespaceId;

    private Long organizationId;

    private Long detailId;

    private Integer isExport;

    public GetArchivesEmployeeCommand() {
    }

    public GetArchivesEmployeeCommand(Long organizationId, Long detailId, Integer isExport) {
        this.organizationId = organizationId;
        this.detailId = detailId;
        this.isExport = isExport;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

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

    public Integer getIsExport() {
        return isExport;
    }

    public void setIsExport(Integer isExport) {
        this.isExport = isExport;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
