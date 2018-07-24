package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;


/**
* <ul>
* <li>event : 获取的事件{@link com.everhomes.rest.yellowPage.ExtraAllianceEventDTO}</li>
* </ul>
*  @author
*  huangmingbo 2018年5月23日
**/
public class GetExtraAllianceEventResponse {
	
	private ExtraAllianceEventDTO event;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public ExtraAllianceEventDTO getEvent() {
		return event;
	}

	public void setEvent(ExtraAllianceEventDTO event) {
		this.event = event;
	}

}

