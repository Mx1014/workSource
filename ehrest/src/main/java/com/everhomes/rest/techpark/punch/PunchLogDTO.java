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
 * <li>requestToken： 异常申请的token </li>
 * <li>approvalStatus： 审批的状态 参考{@link com.everhomes.rest.approval.ApprovalStatus}  </li>
 * <li>smartAlignment： 智能校准 1-是智能校准 0-不是智能校准 </li>
 * <li>identification： 打卡设备的唯一标识 </li>
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
	private Double longitude;
	private Double latitude;
	private String identification;
    private String requestToken;
    private Byte approvalStatus;
    private Byte smartAlignment;

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


	public Integer getPunchIntervalNo() {
		return punchIntervalNo;
	}

	public void setPunchIntervalNo(Integer punchIntervalNo) {
		this.punchIntervalNo = punchIntervalNo;
	}

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	public Byte getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Byte getSmartAlignment() {
		return smartAlignment;
	}

	public void setSmartAlignment(Byte smartAlignment) {
		this.smartAlignment = smartAlignment;
	}


	public Long getRuleTime() {
		return ruleTime;
	}

	public void setRuleTime(Long ruleTime) {
		this.ruleTime = ruleTime;
	}

	public Long getPunchTime() {
		return punchTime;
	}

	public void setPunchTime(Long punchTime) {
		this.punchTime = punchTime;
	}
}
