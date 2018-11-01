package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
* <ul>  
* <li>punchTime：打卡时间</li>
 * <li>clockStatus：打卡状态 参考{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
* </ul>
*/
public class PunchClockResponse {
	private byte punchCode ;
	private String punchTime;
	private Byte clockStatus;
	
	

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


 


	public String getPunchTime() {
		return punchTime;
	}



	public void setPunchTime(String punchTime) {
		this.punchTime = punchTime;
	}





	public byte getPunchCode() {
		return punchCode;
	}





	public void setPunchCode(byte punchCode) {
		this.punchCode = punchCode;
	}

	public Byte getClockStatus() {
		return clockStatus;
	}

	public void setClockStatus(Byte clockStatus) {
		this.clockStatus = clockStatus;
	}
}
