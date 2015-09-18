package com.everhomes.techpark.punch;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 打卡的id</li>
 * <li>companyId： 打卡的公司id</li>
 * <li>check_time： 打卡时间</li>
 * <li>clockStatus：打卡记录的状态 如 上班，下班  参考{@link com.everhomes.techpark.punch.ClockStatus}</li>
 * <li>punchStatus：打卡状态  如 迟到 早退 参考{@link com.everhomes.techpark.punch.PunchStatus}</li>
 * </ul>
 */
public class PunchLogDTO { 
    private long punchTime;
    
    private ClockStatus clockStatus;
    private PunchStatus punchStatus ;
     
	public PunchStatus getPunchStatus() {
		return punchStatus;
	}
	public void setPunchStatus(PunchStatus punchStatus) {
		this.punchStatus = punchStatus;
	}

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public ClockStatus getClockStatus() {
		return clockStatus;
	}
	public void setClockStatus(ClockStatus clockStatus) {
		this.clockStatus = clockStatus;
	}
	public long getPunchTime() {
		return punchTime;
	}
	public void setPunchTime(long punchTime) {
		this.punchTime = punchTime;
	}
    
}
