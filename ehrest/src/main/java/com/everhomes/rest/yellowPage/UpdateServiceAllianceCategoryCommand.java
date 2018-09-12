package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> categoryId: 类型id</li>
 *  <li> name: 类型名称</li>
 *  <li> ownerType: 拥有者类型：现在是community</li>
 *  <li> ownerId: 拥有者ID</li>
 *  <li> parentId: 父id,当前传的实际是服务联盟类型:type</li>
 *  <li> logoUrl: 类型logo的url地址</li>
 *  <li> selectedLogoUrl: 类型logo被选中时的url地址</li>
 *  <li> displayMode: 显示类型 {@link com.everhomes.rest.yellowPage.ServiceAllianceCategoryDisplayMode}</li>
 *  <li> displayDestination: 展示端 {@link com.everhomes.rest.yellowPage.ServiceAllianceCategoryDisplayDestination}</li>
 *  <li> skipType: 只有一个企业时是否跳过列表页，0 不跳； 1 跳过</li>
 *  <li> namespaceId: 域空间id</li>
 * </ul>
 */
public class UpdateServiceAllianceCategoryCommand {
	
	private Long categoryId;
	
	private String name;

	private String ownerType;
	
	private Long ownerId;

	private Long parentId;
	
	private String logoUrl;
	
	private String selectedLogoUrl;

    private Byte displayMode;

	private Byte displayDestination;
	
	private Integer namespaceId;

	private Byte skipType;
	
	public Byte getSkipType() {
		return skipType;
	}

	public void setSkipType(Byte skipType) {
		this.skipType = skipType;
	}

	public Byte getDisplayDestination() {
		return displayDestination;
	}

	public void setDisplayDestination(Byte displayDestination) {
		this.displayDestination = displayDestination;
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
	
	public String getOwnerType() {
		return ownerType;
	}

    public Byte getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(Byte displayMode) {
        this.displayMode = displayMode;
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getSelectedLogoUrl() {
		return selectedLogoUrl;
	}

	public void setSelectedLogoUrl(String selectedLogoUrl) {
		this.selectedLogoUrl = selectedLogoUrl;
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
