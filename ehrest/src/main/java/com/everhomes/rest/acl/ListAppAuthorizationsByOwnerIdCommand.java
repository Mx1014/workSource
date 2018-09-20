package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>ownerId: 应用所属的管理公司，必填</li>
 *     <li>organizationId: 应用授权的目标公司id，ownerId等于organizationId时表示授权给自己的（即是未授权的）,不传则查询管理公司所有的授权（包括自己）</li>
 *     <li>includeAuthToOwnerFlag: 是否包含授权给自己的记录，用于查询所有授权给其他人的记录(传0)，其他情况不传 参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: pageSize</li>
 * </ul>
 */
public class ListAppAuthorizationsByOwnerIdCommand {
    private Integer namespaceId;
    private Long ownerId;
    private Long organizationId;
    private Byte includeAuthToOwnerFlag;
    private Long pageAnchor;
    private Integer pageSize;

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


    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Byte getIncludeAuthToOwnerFlag() {
        return includeAuthToOwnerFlag;
    }

    public void setIncludeAuthToOwnerFlag(Byte includeAuthToOwnerFlag) {
        this.includeAuthToOwnerFlag = includeAuthToOwnerFlag;
    }

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
}
