package com.everhomes.rest.openapi.techpark;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.RestVersion;

public class CustomerResponse extends RestResponseBase {
	private String syncState;
	private Byte dataType;

	public CustomerResponse() {
		this.version = RestVersion.current().toString();
		this.errorCode = Integer.valueOf(200);
	}

	public CustomerResponse(String syncState, Byte dataType) {
		this();
		this.syncState = syncState;
		this.dataType = dataType;
	}

	public String getSyncState() {
		return syncState;
	}

	public void setSyncState(String syncState) {
		this.syncState = syncState;
	}

	public Byte getDataType() {
		return dataType;
	}

	public void setDataType(Byte dataType) {
		this.dataType = dataType;
	}
}
