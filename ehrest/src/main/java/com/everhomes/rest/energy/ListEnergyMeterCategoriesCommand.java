package com.everhomes.rest.energy;

import com.everhomes.rest.energy.util.EnumType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>categoryType: 分类类型 {@link com.everhomes.rest.energy.EnergyCategoryType}</li>
 * </ul>
 */
public class ListEnergyMeterCategoriesCommand {

    @NotNull private Long organizationId;
    @EnumType(value = EnergyCategoryType.class)
    @NotNull private Byte categoryType;

    public Long getOrganizationId() {
        return organizationId;
    }

    public Byte getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Byte categoryType) {
        this.categoryType = categoryType;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
