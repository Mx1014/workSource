// @formatter:off
package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>welfareId: 福利Id</li>
 * <li>appId: 福利appId</li>
 * </ul>
 */
public class DeleteWelfareCommand {

	private Long welfareId;

	private Long appId;

	public DeleteWelfareCommand() {

	}

	public DeleteWelfareCommand(Long welfareId) {
		super();
		this.welfareId = welfareId;
	}

	public Long getWelfareId() {
		return welfareId;
	}

	public void setWelfareId(Long welfareId) {
		this.welfareId = welfareId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
}
