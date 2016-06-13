// @formatter:off
package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 主键id</li>
 * <li>communityId：小区id</li>
 * <li>addressId：地址id</li>
 * <li>name：物业映射地址名称</li>
 * </ul>
 */
public class UpdatePropAddressMappingCommand {
    @NotNull
	private Long   id;
	private Long   communityId;
	private Long   addressId;
	private String name;
	
	public UpdatePropAddressMappingCommand() {
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
