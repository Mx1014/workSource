package com.everhomes.rest.techpark.punch;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>punchIntervalNo：第几个打卡时间段</li> 
 * <li>punchType：上班还是下班 0-上班 1-下班 2-不打卡 参考 {@link PunchType}</li> 
 * <li>ruleTime： 规则时间(设置上下班打卡时间)</li> 
 * <li>punchTime： 实际打卡时间</li> 
 * <li>latitude： 维度</li>
 * <li>longitude： 经度</li>
 * <li>clockStatus：打卡状态 参考{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * </ul>
 */
public class PunchLogDTO {

	private Long expiryTime;
	private Long punchNormalTime;
	private Integer punchIntervalNo;
	private Byte punchType;
	private long ruleTime;
    private long punchTime; 
    private Byte clockStatus;
	private Double longitude;
	private Double latitude;
	private String identification;

      

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
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

	public Integer getPunchIntervalNo() {
		return punchIntervalNo;
	}

	public void setPunchIntervalNo(Integer punchIntervalNo) {
		this.punchIntervalNo = punchIntervalNo;
	}
 
    
}
