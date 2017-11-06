package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>ownerId: 组织id</li>
 *     <li>ownerType: 组织类型</li>
 *     <li>communityId: 小区id</li>
 *     <li>namespaceId: 域空间</li>
 * </ul>
 */
public class ImportEnergyMeterCommand {

    @NotNull private Long ownerId;
    @NotNull private String ownerType;
    @NotNull private Long communityId;
    private Integer namespaceId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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
}
