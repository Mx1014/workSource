package com.everhomes.rest.techpark.punch;



/**
 * <ul>
 * <li>userId：用户id</li>
 * <li>userName：名称</li>
 * <li>token：联系电话</li>
 * <li>deptName：部门</li>
 * <li>workDayCount:应上班天数</li>
 * <li>workCount:实际上班天数</li>
 * <li>belateCount：迟到天数</li>
 * <li>leaveEarlyCount：早退天数</li>
 * <li>unPunchCount：未打卡天数</li>
 * <li>blandleCount：迟到且早退天数</li>
 * <li>absenceCount：事假天数</li>
 * <li>sickCount：病假天数</li>
 * <li>exchangeCount：调休天数</li>
 * <li>outworkCount：公出天数</li>
 * </ul>
 */
public class PunchCountDTO {
	private Long userId;
	private String userName;
	private String token;
	private String deptName;
    private Integer workDayCount;
    private Double workCount;
    private Integer belateCount;
    private Integer leaveEarlyCount;
    private Double unpunchCount;
    private Integer blandleCount;
    private Double absenceCount;
    private Double sickCount;
    private Double exchangeCount;
    private Double outworkCount;
    private Long overTimeSum;
	private java.lang.Byte     punchTimesPerDay;
	private String userEnterpriseGroup;
	
	
    public Integer getWorkDayCount() {
		return workDayCount;
	}
	public void setWorkDayCount(Integer workDayCount) {
		this.workDayCount = workDayCount;
	} 
	public Integer getBelateCount() {
		return belateCount;
	}
	public void setBelateCount(Integer belateCount) {
		this.belateCount = belateCount;
	}
	public Integer getLeaveEarlyCount() {
		return leaveEarlyCount;
	}
	public void setLeaveEarlyCount(Integer leaveEarlyCount) {
		this.leaveEarlyCount = leaveEarlyCount;
	}
	public Double getUnpunchCount() {
		return unpunchCount;
	}
	public void setUnpunchCount(Double unPunchCount) {
		this.unpunchCount = unPunchCount;
	}
	public Integer getBlandleCount() {
		return blandleCount;
	}
	public void setBlandleCount(Integer blandleCount) {
		this.blandleCount = blandleCount;
	} 
	
	public Double getAbsenceCount() {
		return absenceCount;
	}
	public void setAbsenceCount(Double absenceCount) {
		this.absenceCount = absenceCount;
	}
	public Double getSickCount() {
		return sickCount;
	}
	public void setSickCount(Double sickCount) {
		this.sickCount = sickCount;
	}
	public Double getExchangeCount() {
		return exchangeCount;
	}
	public void setExchangeCount(Double exchangeCount) {
		this.exchangeCount = exchangeCount;
	}
	public Double getOutworkCount() {
		return outworkCount;
	}
	public void setOutworkCount(Double outworkCount) {
		this.outworkCount = outworkCount;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Long getOverTimeSum() {
		return overTimeSum;
	}
	public void setOverTimeSum(Long overTimeSum) {
		this.overTimeSum = overTimeSum;
	}
	public java.lang.Byte getPunchTimesPerDay() {
		return punchTimesPerDay;
	}
	public void setPunchTimesPerDay(java.lang.Byte punchTimesPerDay) {
		this.punchTimesPerDay = punchTimesPerDay;
	}
	public String getUserEnterpriseGroup() {
		return userEnterpriseGroup;
	}
	public void setUserEnterpriseGroup(String userEnterpriseGroup) {
		this.userEnterpriseGroup = userEnterpriseGroup;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Double getWorkCount() {
		return workCount;
	}
	public void setWorkCount(Double workCount) {
		this.workCount = workCount;
	}
}
