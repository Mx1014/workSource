// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>addressId: 地址id</li>
 * <li>status: 该地址的居住状态</li>
 * </ul>
 */
public class SetPropAddressStatusCommand {
	
	private Long organizationId;
	
    private Long addressId;
    
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
