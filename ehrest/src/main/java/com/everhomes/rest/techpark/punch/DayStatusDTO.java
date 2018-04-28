package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>  
 * <li>date：日期</li>
 * <li>status：状态</li> 
 * </ul>
 */
public class DayStatusDTO {
	private String date;
	private String status;


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}

