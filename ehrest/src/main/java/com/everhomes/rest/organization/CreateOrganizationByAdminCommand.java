package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>parentId：父机构id。没有填0</li>
 * <li>name：名称</li>
 * <li>addressId：组织地址Id</li>
 * <li>OrganizationType：组织类型：参考{@link com.everhomes.rest.organization.OrganizationType}</li>
 * <li>communityId：小区id</li>
 * </ul>
 */
public class CreateOrganizationByAdminCommand {
	
	private Long    parentId;
	@NotNull
	private String  name;
	private Long    addressId;
	@NotNull
	private String   OrganizationType;
	
	private Long communityId;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getOrganizationType() {
		return OrganizationType;
	}

	public void setOrganizationType(String organizationType) {
		OrganizationType = organizationType;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
}
