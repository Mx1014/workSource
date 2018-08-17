package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

public class FindReservationsCommand {
	
	private Long addressId;
	private Byte status;
	
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
