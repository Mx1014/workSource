// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>status: 状态，参考{@link com.everhomes.rest.group.GroupMemberStatus}</li>
 * </ul>
 */
public class GetRequestInfoResponse {
	private Byte status;

	private String text;

	public GetRequestInfoResponse() {
		super();
	}

	public GetRequestInfoResponse(Byte status) {
		super();
		this.status = status;
	}
	public GetRequestInfoResponse(Byte status, String text) {
		super();
		this.status = status;
		this.text = text;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
