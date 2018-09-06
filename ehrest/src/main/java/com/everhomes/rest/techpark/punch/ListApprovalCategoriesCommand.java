package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID,必填</li>
 * <li>ownerType: 参考{@link com.everhomes.rest.approval.ApprovalOwnerType}，默认</li>
 * <li>ownerId: 总公司ID，必填</li>
 * </ul>
 */
public class ListApprovalCategoriesCommand {
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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
