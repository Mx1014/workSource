// @formatter:off
package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>addressId: 地址id</li>
 * <li>status: 该地址的居住状态,{@link com.everhomes.rest.organization.pm.AddressMappingStatus}</li>
 * </ul>
 */
public class SetPropAddressStatusCommand {
	@NotNull
	private Long organizationId;
	@NotNull
    private Long addressId;
    @NotNull
    private Byte status;
   
    public SetPropAddressStatusCommand() {
    }

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
	
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
