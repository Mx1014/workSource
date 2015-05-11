// @formatter:off
package com.everhomes.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>addressId: 地址id</li>
 * <li>status: 该地址的居住状态</li>
 * </ul>
 */
public class SetPropAddressStatusCommand {
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
