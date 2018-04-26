package com.everhomes.rest.fixedasset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>ownerType: 默认EhOrganizations</li>
 * <li>ownerId: 公司ID，必填</li>
 * <li>fixedAssetIds: 要删除的资产ID列表</li>
 * </ul>
 */
public class BatchDeleteFixedAssetCommand {
    private String ownerType;
    private Long ownerId;
    @ItemType(Long.class)
    private List<Long> fixedAssetIds;

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

    public List<Long> getFixedAssetIds() {
        return fixedAssetIds;
    }

    public void setFixedAssetIds(List<Long> fixedAssetIds) {
        this.fixedAssetIds = fixedAssetIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
