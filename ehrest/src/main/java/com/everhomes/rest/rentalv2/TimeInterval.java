package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

public class TimeInterval {

	private Double beginTime; 
	private Double endTime;


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Double getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Double beginTime) {
		this.beginTime = beginTime;
	}

	public Double getEndTime() {
		return endTime;
	}

	public void setEndTime(Double endTime) {
		this.endTime = endTime;
	}


}
