package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

/**
 * @author 黄明波
 *
 */
public enum PrivilegeType {

	/*********服务热线**********/
	PUBLIC_HOTLINE(4030040310L, "公共热线-全部权限"), 
	EXCLUSIVE_SERVICER_MANAGE(4030040320L,  "专属客服-客服管理"), 
	EXCLUSIVE_SERVICE_CHAT_RECORD(4030040321L, "专属客服-历史会话"),
	
	/*********服务联盟**********/
	SERVICE_MANAGE(4050040520L, "服务联盟-服务管理"), 
	APPLY_RECORD(4050040540L,  "服务联盟-申请记录"), 
	INFO_NOTIFY(4050040530L, "服务联盟-消息通知"),
	USER_BEHAVIOUR_STAT(4050040550L, "服务联盟-用户行为统计");
	

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
