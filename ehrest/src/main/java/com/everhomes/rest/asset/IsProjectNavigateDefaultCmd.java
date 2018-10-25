//@formatter:off
package com.everhomes.rest.asset;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 下午2:11:14
 */

/**
 *<ul>
 * <li>moduleType:模块类型，参考{@link com.everhomes.rest.asset.AssetModuleType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type</li>
 * <li>namespaceId: 域名</li>
 * <li>organizationId: 标准版新增的管理公司ID</li>
 *</ul>
 */
public class IsProjectNavigateDefaultCmd {
	private String moduleType;
	@NotNull
    private Long ownerId;
    @NotNull
    private String ownerType;
    @NotNull
    private Integer namespaceId;
    private Long categoryId;
    private Long organizationId;//标准版新增的管理公司ID

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

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
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
}
