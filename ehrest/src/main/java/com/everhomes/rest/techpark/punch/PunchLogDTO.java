package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>expiryTime：状态终止时间(APP在超过这个时间之后要重新请求数据更新页面)</li> 
 * <li>punchDayType：工作日/休息日/节假日  参考{@link com.everhomes.rest.techpark.punch.PunchDayType}</li>
 * <li>punchIntervalNo：第几个打卡时间段</li> 
 * <li>punchType：上班还是下班 0-上班 1-下班 2-不打卡 参考 {@link PunchType}</li> 
 * <li>ruleTime： 规则时间(设置上下班打卡时间)</li> 
 * <li>punchTime： 实际打卡时间</li> 
 * <li>shouldPunchTime：应打卡时间(迟到早退用)</li> 
 * <li>latitude： 维度</li>
 * <li>longitude： 经度</li>
 * <li>clockStatus：打卡状态 参考{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>statusString：打卡状态文字</li>
 * <li>requestToken： 异常申请的token </li>
 * <li>approvalStatus： 审批的状态 参考{@link com.everhomes.rest.approval.ApprovalStatus}  </li>
 * <li>smartAlignment： 智能校准 1-是智能校准 0-不是智能校准 </li>
 * <li>identification： 打卡设备的唯一标识 </li>
 * <li>userName： 用户姓名 </li>
 * <li>deptName：部门名 </li>
 * <li>ruleName：规则名称 </li>
 * <li>locationInfo：打卡地点信息(有地点以地点打卡为准,没有地点才是wifi打卡) </li>
 * <li>wifiInfo：打卡Wifi信息(有地点以地点打卡为准,没有地点才是wifi打卡) </li>
 * <li>deviceChangeFlag：设备异常 0-否 1-是 </li>
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
    private String statusString;
	private Double longitude;
	private Double latitude;
	private String identification;
    private String requestToken;
    private Byte approvalStatus;
    private Byte smartAlignment;

    private Long shouldPunchTime;
    private Long punchDate;

    private Long userId;
    private String userName;
    private String deptName;
    private String ruleName;

    private Byte punchDayType;
    private String locationInfo;
    private String wifiInfo;

    private Byte deviceChangeFlag;
	private Long punchOrgnizationId;
	private Long punchRuleId;
	private Long punchRuleTimeId;
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getIdentification() {
		return identification;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}

	public Byte getDeviceChangeFlag() {
		return deviceChangeFlag;
	}

	public void setDeviceChangeFlag(Byte deviceChangeFlag) {
		this.deviceChangeFlag = deviceChangeFlag;
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

	public Long getShouldPunchTime() {
		return shouldPunchTime;
	}

	public void setShouldPunchTime(Long shouldPunchTime) {
		this.shouldPunchTime = shouldPunchTime;
	}

	public Long getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Long punchDate) {
		this.punchDate = punchDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Byte getPunchDayType() {
		return punchDayType;
	}

	public void setPunchDayType(Byte punchDayType) {
		this.punchDayType = punchDayType;
	}

	public String getWifiInfo() {
		return wifiInfo;
	}

	public void setWifiInfo(String wifiInfo) {
		this.wifiInfo = wifiInfo;
	}


	public Long getPunchOrgnizationId() {
		return punchOrgnizationId;
	}

	public void setPunchOrgnizationId(Long punchOrgnizationId) {
		this.punchOrgnizationId = punchOrgnizationId;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public Long getPunchRuleId() {
		return punchRuleId;
	}

	public void setPunchRuleId(Long punchRuleId) {
		this.punchRuleId = punchRuleId;
	}

	public Long getPunchRuleTimeId() {
		return punchRuleTimeId;
	}

	public void setPunchRuleTimeId(Long punchRuleTimeId) {
		this.punchRuleTimeId = punchRuleTimeId;
	}
}
