package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <ul>
 *     <li>ownerId: 分类所属组织id</li>
 *     <li>ownerType: 分类所属组织类型</li>
 *     <li>communityId: 分类所属园区id</li>
 *     <li>name: 名称</li>
 *     <li>categoryType: 类型 {@link com.everhomes.rest.energy.EnergyCategoryType}</li>
 * </ul>
 */
public class CreateEnergyMeterCategoryCommand {

    @NotNull private Long ownerId;
    @NotNull private String ownerType;
    @NotNull private Long communityId;
    @NotNull @Size(max = 255) private String name;
    @NotNull private Byte categoryType;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Byte categoryType) {
        this.categoryType = categoryType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
