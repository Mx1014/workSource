package com.everhomes.rest.fixedasset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 默认EhOrganizations</li>
 * <li>ownerId: 公司ID，必填</li>
 * <li>fixedAssetId: 要删除的资产ID</li>
 * </ul>
 */
public class DeleteFixedAssetCommand {
    private String ownerType;
    private Long ownerId;
    private Long fixedAssetId;

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

    public Long getFixedAssetId() {
        return fixedAssetId;
    }

    public void setFixedAssetId(Long fixedAssetId) {
        this.fixedAssetId = fixedAssetId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
