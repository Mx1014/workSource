package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class PunchClockRecordData implements Serializable{
	
	private static final long serialVersionUID = 1146179811681362554L;
	private Long organizationId;
	private Long userId;
	private Long date;
	
    public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
