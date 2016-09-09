package com.everhomes.rest.ui.user;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>location: 活动地点</li>
 * <li>startTime: 活动开始时间</li>
 *</ul>
 */
public class ActivityFootnote {

	private String location;
	
	private String startTime;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
