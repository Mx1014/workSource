package com.everhomes.rest.techpark.punch;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>punchTime： 打卡时间</li> 
 * <li>clockStatus：打卡记录的状态 如 上班，下班  参考{@link com.everhomes.rest.techpark.punch.ClockStatus}</li>
 * </ul>
 */
public class PunchLogDTO { 
    private long punchTime;
    
    private Byte clockStatus;
      

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	 
	public long getPunchTime() {
		return punchTime;
	}
	public void setPunchTime(long punchTime) {
		this.punchTime = punchTime;
	}

	public Byte getClockStatus() {
		return clockStatus;
	}

	public void setClockStatus(Byte clockStatus) {
		this.clockStatus = clockStatus;
	}
 
    
}
