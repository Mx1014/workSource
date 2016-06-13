package com.everhomes.rest.videoconf;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>reserveConfId: 预约会议的会议id</li>
 * </ul>
 *
 */
public class CreateVideoConfInvitationCommand {
	
	@NotNull
	private Long reserveConfId;
	
	public Long getReserveConfId() {
		return reserveConfId;
	}

	public void setReserveConfId(Long reserveConfId) {
		this.reserveConfId = reserveConfId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
