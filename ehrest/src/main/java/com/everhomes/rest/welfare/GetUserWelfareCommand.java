// @formatter:off
package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>welfareId: 福利Id</li>
 * </ul>
 */
public class GetUserWelfareCommand {

	private Long welfareId;

	public GetUserWelfareCommand() {

	}

	public GetUserWelfareCommand(Long welfareId) {
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

}
