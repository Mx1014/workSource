package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>orgId: 管理公司id</li>
 * <li>ownerType: 所属类型</li>
 * <li>ownerId: 所属项目</li>
 * </ul>
 */
public class CopyCitiesCommand {

    private Integer namespaceId;
    private Long orgId;
    private String ownerType;
    private Long ownerId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
