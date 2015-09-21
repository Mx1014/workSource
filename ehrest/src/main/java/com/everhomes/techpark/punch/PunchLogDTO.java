package com.everhomes.techpark.punch;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>punchTime： 打卡时间</li> 
 * <li>clockStatus：打卡记录的状态 如 上班，下班  参考{@link com.everhomes.techpark.punch.ClockStatus}</li>
 * <li>punchStatus：打卡状态  如 迟到 早退 参考{@link com.everhomes.techpark.punch.PunchStatus}</li>
 * </ul>
 */
public class PunchLogDTO { 
    private long punchTime;
    
    private byte clockStatus;
    private byte punchStatus ;
      

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

	public byte getClockStatus() {
		return clockStatus;
	}

	public void setClockStatus(byte clockStatus) {
		this.clockStatus = clockStatus;
	}

	public byte getPunchStatus() {
		return punchStatus;
	}

	public void setPunchStatus(byte punchStatus) {
		this.punchStatus = punchStatus;
	}
    
}
