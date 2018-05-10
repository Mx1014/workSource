package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>ownerId: 应用所属的管理公司，必填</li>
 *     <li>organizationId: 应用授权的目标公司id，ownerId等于organizationId时表示授权给自己的（即是未授权的）,不传则查询管理公司所有的授权（包括自己）</li>
 * </ul>
 */
public class ListAppAuthorizationsByOwnerIdCommand {
    private Integer namespaceId;
    private Long ownerId;
    private Long organizationId;

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
}
