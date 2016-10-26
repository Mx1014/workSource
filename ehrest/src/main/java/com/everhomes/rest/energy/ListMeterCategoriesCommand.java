package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>categoryType: 分类类型 {@link com.everhomes.rest.energy.EnergyCategoryType}</li>
 * </ul>
 */
public class ListMeterCategoriesCommand {

    @NotNull private Long organizationId;
    @NotNull private Long categoryType;

    public Long getOrganizationId() {
        return organizationId;
    }

    public Long getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Long categoryType) {
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
