package com.everhomes.rest.haian;

import com.everhomes.util.StringHelper;

public class EncryptForInsurobotCommand {
	private String carNo;

	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
