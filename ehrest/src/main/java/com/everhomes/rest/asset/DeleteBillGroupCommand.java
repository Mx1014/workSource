//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/10/17.
 */
/**
 *<ul>
 * <li>billGroupId:账单组id</li>
 * <li>categoryId: 缴费多应用ID</li>
 * <li>organizationId:管理公司ID</li>
 * <li>appId:应用ID</li>
 * <li>allScope: 标准版增加的allScope参数，true：默认/全部，false：具体项目</li>
 *</ul>
 */
public class DeleteBillGroupCommand {
    private Long billGroupId;

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    
    private Long categoryId;
    private Long organizationId;
    private Long appId;
    private Boolean allScope;//标准版增加的allScope参数，true：默认/全部，false：具体项目

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
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
