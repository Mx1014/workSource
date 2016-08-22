package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhPmTasks;
import com.everhomes.util.StringHelper;

public class PmTask extends EhPmTasks{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nickName;
	
	private String mobile;
	
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	} 
}
