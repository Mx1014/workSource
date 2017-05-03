// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>status: 状态，参考{@link com.everhomes.rest.group.GroupMemberStatus}</li>
 * </ul>
 */
public class GetReqeustInfoResponse {
	private Byte status;

	public GetReqeustInfoResponse() {
		super();
	}

	public GetReqeustInfoResponse(Byte status) {
		super();
		this.status = status;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
