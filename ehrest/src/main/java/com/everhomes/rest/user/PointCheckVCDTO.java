package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class PointCheckVCDTO {
	private Boolean resultCode ;
	private String result ;
	
	public Boolean getResultCode() {
		return resultCode;
	}
	public void setResultCode(Boolean resultCode) {
		this.resultCode = resultCode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
