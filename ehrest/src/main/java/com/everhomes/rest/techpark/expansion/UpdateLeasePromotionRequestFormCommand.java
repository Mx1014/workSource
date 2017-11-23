package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId：域空间id</li>
 * <li>ownerType：ownerType</li>
 * <li>ownerId：ownerId</li>
 * <li>sourceId：表单id</li>
 * <li>sourceType：类型</li>
 * </ul>
 */
public class UpdateLeasePromotionRequestFormCommand {

    private Integer namespaceId;
    private Long ownerId;

    private String ownerType;

    private Long sourceId;

    private String sourceType;

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

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
