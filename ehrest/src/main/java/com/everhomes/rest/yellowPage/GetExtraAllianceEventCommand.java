package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
* <ul>
* <li>eventId : 事件ID</li>
* </ul>
*  @author
*  huangmingbo 2018年5月22日
**/
public class GetExtraAllianceEventCommand {

	@NotNull
	private Long eventId;
	
	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


}
