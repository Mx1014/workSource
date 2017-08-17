package com.everhomes.rest.techpark.punch;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>punchIntevalNo：第几个打卡时间段</li> 
 * <li>punchType：上班还是下班 0-上班 1-下班 2-不打卡 参考 {@link PunchType}</li> 
 * <li>ruleTime： 规则时间(设置上下班打卡时间)</li> 
 * <li>punchTime： 实际打卡时间</li> 
 * <li>clockStatus：打卡记录的状态 如 上班，下班  参考{@link com.everhomes.rest.techpark.punch.ClockStatus}</li>
 * </ul>
 */
public class PunchLogDTO { 
	private Integer punchIntevalNo;
	private Byte punchType;
	private long ruleTime;
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

	public Integer getPunchIntevalNo() {
		return punchIntevalNo;
	}

	public void setPunchIntevalNo(Integer punchIntevalNo) {
		this.punchIntevalNo = punchIntevalNo;
	}

	public Byte getPunchType() {
		return punchType;
	}

	public void setPunchType(Byte punchType) {
		this.punchType = punchType;
	}

	public long getRuleTime() {
		return ruleTime;
	}

	public void setRuleTime(long ruleTime) {
		this.ruleTime = ruleTime;
	}
 
    
}
