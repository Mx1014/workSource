// @formatter:off
package com.everhomes.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 主键id</li>
 * <li>communityId：小区id</li>
 * <li>addressId：地址id</li>
 * <li>name：物业映射名称</li>
 * </ul>
 */
public class UpdatePropAddressMappingCommand {
    @NotNull
	private java.lang.Long   id;
	private java.lang.Long   communityId;
	private java.lang.Long   addressId;
	private java.lang.String name;
	
	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(java.lang.Long communityId) {
		this.communityId = communityId;
	}

	public java.lang.Long getAddressId() {
		return addressId;
	}

	public void setAddressId(java.lang.Long addressId) {
		this.addressId = addressId;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
