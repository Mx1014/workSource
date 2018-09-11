package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.energy.util.EnumType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>ownerId: 组织id</li>
 *     <li>ownerType: 组织类型</li>
 *     <li>communityId: 园区</li>
 *     <li>categoryType: 分类类型 {@link com.everhomes.rest.energy.EnergyCategoryType}</li>
 *     <li>namespaceId: 域空间</li>
 * </ul>
 */
public class ListEnergyMeterCategoriesCommand {

    @NotNull private Long ownerId;
    @NotNull private String ownerType;
    @ItemType(Long.class)
    private List<Long> communityIds;
    @EnumType(value = EnergyCategoryType.class)
    @NotNull private Byte categoryType;
    
    private Integer namespaceId;

    public List<Long> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(List<Long> communityIds) {
        this.communityIds = communityIds;
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

    public Byte getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Byte categoryType) {
        this.categoryType = categoryType;
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
