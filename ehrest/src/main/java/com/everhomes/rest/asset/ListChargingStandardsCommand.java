//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type community</li>
 * <li>namespaceId:域空间</li>
 * <li>chargingItemId:收费项目id</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:每页大小</li>
 * <li>organizationId:管理公司ID</li>
 * <li>appId:应用ID</li>
 *</ul>
 */
public class ListChargingStandardsCommand {
    @NotNull
    private Long chargingItemId;
    @NotNull
    private Long ownerId;
    @NotNull
    private String ownerType;
    private Long pageAnchor;
    private Integer pageSize;

    private Integer namespaceId;
    private Long categoryId;
    private Long moduleId;
    private Long billGroupId;
    
    private Long organizationId;
    private Long appId;
    private Boolean allScope;//标准版增加的allScope参数，true：默认/全部，false：具体项目

    public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }


    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }


    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getChargingItemId() {
        return chargingItemId;
    }

    public void setChargingItemId(Long chargingItemId) {
        this.chargingItemId = chargingItemId;
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

    public ListChargingStandardsCommand() {

    }

	public Long getBillGroupId() {
		return billGroupId;
	}

	public void setBillGroupId(Long billGroupId) {
		this.billGroupId = billGroupId;
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
