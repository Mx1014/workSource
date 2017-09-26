package com.everhomes.rest.techpark.punch;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>punchIntervalNo：第几个打卡时间段</li> 
 * <li>punchType：上班还是下班 0-上班 1-下班 2-不打卡 参考 {@link PunchType}</li> 
 * <li>ruleTime： 规则时间(设置上下班打卡时间)</li> 
 * <li>punchTime： 实际打卡时间</li> 
 * <li>clockStatus：打卡状态 参考{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * </ul>
 */
public class PunchLogDTO {

	private Long expiryTime;
	private Long punchNormalTime;
	private Integer punchIntervalNo;
	private Byte punchType;
	private Long ruleTime;
    private Long punchTime;
    private Byte clockStatus;
      

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Long expiryTime) {
		this.expiryTime = expiryTime;
	}

	public Long getPunchNormalTime() {
		return punchNormalTime;
	}

	public void setPunchNormalTime(Long punchNormalTime) {
		this.punchNormalTime = punchNormalTime;
	}

	public Long getPunchTime() {
		return punchTime;
	}
	public void setPunchTime(Long punchTime) {
		this.punchTime = punchTime;
	}

	public Byte getClockStatus() {
		return clockStatus;
	}

	public void setClockStatus(Byte clockStatus) {
		this.clockStatus = clockStatus;
	}
 

	public Byte getPunchType() {
		return punchType;
	}

	public void setPunchType(Byte punchType) {
		this.punchType = punchType;
	}

	public Long getRuleTime() {
		return ruleTime;
	}

	public void setRuleTime(Long ruleTime) {
		this.ruleTime = ruleTime;
	}

	public Integer getPunchIntervalNo() {
		return punchIntervalNo;
	}

	public void setPunchIntervalNo(Integer punchIntervalNo) {
		this.punchIntervalNo = punchIntervalNo;
	}
 
    
}
