package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>name: 名称</li>
 *     <li>categoryType: 类型 {@link com.everhomes.rest.energy.EnergyCategoryType}</li>
 * </ul>
 */
public class CreateEnergyMeterCategoryCommand {

    @NotNull private Long organizationId;
    @NotNull @Size(max = 255) private String name;
    @NotNull private Byte categoryType;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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
