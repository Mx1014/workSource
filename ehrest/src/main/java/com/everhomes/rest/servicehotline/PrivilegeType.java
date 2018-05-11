package com.everhomes.rest.servicehotline;

import com.everhomes.util.StringHelper;

/**
 * @author 黄明波
 *
 */
public enum PrivilegeType {

	PUBLIC_HOTLINE(4030040310L, "公共热线-全部权限"), 
	EXCLUSIVE_SERVICER_MANAGE(4030040320L,  "专属客服-客服管理"), 
	EXCLUSIVE_SERVICE_CHAT_RECORD(4030040321L, "专属客服-历史会话");

	private long code;
	private String info; // 说明

	private PrivilegeType(long code, String info) {
		this.code = code;
		this.info = info;
	}

	public long getCode() {
		return this.code;
	}

	public String getInfo() {
		return this.info;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
