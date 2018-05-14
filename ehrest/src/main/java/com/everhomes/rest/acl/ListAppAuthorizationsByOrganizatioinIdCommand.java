package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id，必填</li>
 *     <li>organizationId: 本公司Id，必填</li>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: pageSize</li>
 * </ul>
 */
public class ListAppAuthorizationsByOrganizatioinIdCommand {
    private Integer namespaceId;
    private Long organizationId;
    private Long pageAnchor;
    private Integer pageSize;

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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
}
