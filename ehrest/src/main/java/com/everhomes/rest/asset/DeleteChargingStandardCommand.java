//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type</li>
 * <li>chargingStandardId: 收费标准id</li>
 * <li>organizationId:管理公司ID</li>
 * <li>appId:应用ID</li>
 * <li>allScope:标准版增加的allScope参数，true：默认/全部，false：具体项目</li>
 *</ul>
 */
public class DeleteChargingStandardCommand {
    @NotNull
    private Long ownerId;
    @NotNull
    private String ownerType;
    @NotNull
    private Long chargingStandardId;
    private Integer namespaceId;
    private Long categoryId;
    
    private Long organizationId;
    private Long appId;
    private Boolean allScope;//标准版增加的allScope参数，true：默认/全部，false：具体项目

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

    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public void setChargingStandardId(Long chargingStandardId) {
        this.chargingStandardId = chargingStandardId;
    }

    public DeleteChargingStandardCommand() {

    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Boolean getAllScope() {
		return allScope;
	}

	public void setAllScope(Boolean allScope) {
		this.allScope = allScope;
	}
}
