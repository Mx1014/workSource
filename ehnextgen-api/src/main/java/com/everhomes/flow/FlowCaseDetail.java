package com.everhomes.flow;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

public class FlowCaseDetail extends FlowCase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2701667492181821425L;
	
	private Long eventLogId;

	public Long getEventLogId() {
		return eventLogId;
	}
	public void setEventLogId(Long eventLogId) {
		this.eventLogId = eventLogId;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
