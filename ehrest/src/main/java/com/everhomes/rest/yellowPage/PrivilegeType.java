package com.everhomes.rest.yellowPage;
import com.everhomes.util.StringHelper;

/**
 * @author 黄明波
 *
 */
public enum PrivilegeType {

	SERVICE_MANAGE(4050040520L, "服务联盟-服务管理"), 
	APPLY_RECORD(4050040540L,  "服务联盟-申请记录"), 
	INFO_NOTIFY(4050040530L, "服务联盟-消息通知");

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
