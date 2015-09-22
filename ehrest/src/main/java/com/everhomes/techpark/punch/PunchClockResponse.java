package com.everhomes.techpark.punch;

import com.everhomes.util.StringHelper;

/**
* <ul>  
* <li>code：</li>
* </ul>
*/
public class PunchClockResponse {
	private ClockCode code ;
	private String punchTime;
	
	

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public ClockCode getCode() {
		return code;
	}



	public void setCode(ClockCode code) {
		this.code = code;
	}



	public String getPunchTime() {
		return punchTime;
	}



	public void setPunchTime(String punchTime) {
		this.punchTime = punchTime;
	}
}
