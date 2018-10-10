//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type</li>
 * <li>namespaceId: 域名</li>
 * <li>categoryId: 多入口id</li>
 * <li>moduleId: 模块id</li>
 * <li>billGroupId: 账单组id</li>
 * <li>organizationId: 标准版新增的管理公司ID</li>
 * <li>allScope: 标准版增加的allScope参数，true：默认/全部，false：具体项目</li>
 *</ul>
 */
public class OwnerIdentityCommand {
    @NotNull
    private Long ownerId;
    @NotNull
    private String ownerType;
    @NotNull
    private Integer namespaceId;
    private Long categoryId;
    private Long moduleId;
    private Long billGroupId;
    
    private Long organizationId;//标准版新增的管理公司ID
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

    public Boolean getAllScope() {
        return allScope;
    }

    public void setAllScope(Boolean allScope) {
        this.allScope = allScope;
    }

    public OwnerIdentityCommand() {

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
}
