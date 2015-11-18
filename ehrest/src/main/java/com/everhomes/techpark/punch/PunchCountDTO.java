package com.everhomes.techpark.punch;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId：申请人id</li>
 * <li>userName：申请人名称</li>
 * <li>token：申请人联系电话</li>
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
    private Integer workDayCount;
    private Integer workCount;
    private Integer belateCount;
    private Integer leaveEarlyCount;
    private Integer unPunchCount;
    private Integer blandleCount;
    private Integer absenceCount;
    private Integer sickCount;
    private Integer exchangeCount;
    private Integer outworkCount;
    private Long overTimeSum;
    public Integer getWorkDayCount() {
		return workDayCount;
	}
	public void setWorkDayCount(Integer workDayCount) {
		this.workDayCount = workDayCount;
	}
	public Integer getWorkCount() {
		return workCount;
	}
	public void setWorkCount(Integer workCount) {
		this.workCount = workCount;
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
	public Integer getUnPunchCount() {
		return unPunchCount;
	}
	public void setUnPunchCount(Integer unPunchCount) {
		this.unPunchCount = unPunchCount;
	}
	public Integer getBlandleCount() {
		return blandleCount;
	}
	public void setBlandleCount(Integer blandleCount) {
		this.blandleCount = blandleCount;
	}
	public Integer getAbsenceCount() {
		return absenceCount;
	}
	public void setAbsenceCount(Integer absenceCount) {
		this.absenceCount = absenceCount;
	}
	public Integer getSickCount() {
		return sickCount;
	}
	public void setSickCount(Integer sickCount) {
		this.sickCount = sickCount;
	}
	public Integer getExchangeCount() {
		return exchangeCount;
	}
	public void setExchangeCount(Integer exchangeCount) {
		this.exchangeCount = exchangeCount;
	}
	public Integer getOutworkCount() {
		return outworkCount;
	}
	public void setOutworkCount(Integer outworkCount) {
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
}
