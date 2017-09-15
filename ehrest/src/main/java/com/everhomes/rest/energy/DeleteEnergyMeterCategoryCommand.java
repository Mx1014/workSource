package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>categoryId: 分类id</li>
 *     <li>namespaceId: 域空间</li>
 * </ul>
 */
public class DeleteEnergyMeterCategoryCommand {

    @NotNull private Long organizationId;
    @NotNull private Long categoryId;
    private Integer namespaceId;

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
