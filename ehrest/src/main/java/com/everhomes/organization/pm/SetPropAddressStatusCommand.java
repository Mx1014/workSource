// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>addressId: 地址id</li>
 * <li>status: 该地址的居住状态</li>
 * </ul>
 */
public class SetPropAddressStatusCommand {
	
	private Long communityId;
	
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
