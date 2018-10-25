//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/2/8.
 */

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type community</li>
 * <li>namespaceId: 域名</li>
 *</ul>
 */
public class ListLateFineStandardsCommand {
    @NotNull
    private Long ownerId;
    @NotNull
    private String ownerType;
    @NotNull
    private Integer namespaceId;
    private Long categoryId;
    private Long moduleId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

}
