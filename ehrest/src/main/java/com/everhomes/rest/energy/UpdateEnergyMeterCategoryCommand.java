package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>categoryId: 分类id</li>
 *     <li>name: 名称</li>
 * </ul>
 */
public class UpdateEnergyMeterCategoryCommand {

    @NotNull private Long organizationId;
    @NotNull private Long categoryId;
    @NotNull @Size(max = 255) private String name;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
