package com.everhomes.rest.uniongroup;

import com.everhomes.util.StringHelper;

/**
 * <li>organizaitonId 公司id</li>
 * <li>namespaceId 域空间</li>
 * <li>contactName 联系人</li>
 */
public class ListDetailsNotInUniongroupsCommand {

    public ListDetailsNotInUniongroupsCommand() {

    }

    private Long organizationId;
    private Integer namespaceId;
    private String contactName;
    private Integer versionCode;
    private Long departmentId;
    private Integer pageSize;
    private Long pageAnchor;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }
}
