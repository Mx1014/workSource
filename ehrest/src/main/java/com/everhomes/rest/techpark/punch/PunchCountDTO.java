package com.everhomes.rest.techpark.punch;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;



/**
 * <ul>
 * <li>userId：用户id</li>
 * <li>userName：名称</li>
 * <li>punchMonth：打卡月份</li>
 * <li>token：联系电话</li>
 * <li>deptName：部门</li>
 * <li>punchOrgName：所属规则</li>
 * <li>workDayCount:应上班天数</li>
 * <li>exceptionDayCount:异常天数</li>
 * <li>workCount:正常天数</li>
 * <li>belateCount：迟到天数</li>
 * <li>leaveEarlyCount：早退天数</li>
 * <li>unPunchCount：未打卡天数</li>
 * <li>blandleCount：迟到且早退天数</li> 
 * <li>exts：附加请假exts {@link com.everhomes.rest.techpark.punch.ExtDTO}</li>
 * <li>userStatus：用户状态{@link com.everhomes.rest.techpark.punch.PunchUserStatus} </li> 
 * </ul>
 */
public class PunchCountDTO {
	private Long userId;
	private String userName;
	private String punchMonth;
	private String punchOrgName;
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
    private Double overTimeSum;
	private java.lang.Byte     punchTimesPerDay;
	private String userEnterpriseGroup;
	@ItemType(AbsenceTimeDTO.class)
	private List<AbsenceTimeDTO> absenceTimeList;
	@ItemType(ExtDTO.class)
	private List<ExtDTO> exts;
	private Byte userStatus;

	private Integer exceptionDayCount;

	public Integer getExceptionDayCount() {
		return exceptionDayCount;
	}

	public void setExceptionDayCount(Integer exceptionDayCount) {
		this.exceptionDayCount = exceptionDayCount;
	}

    public List<AbsenceTimeDTO> getAbsenceTimeList() {
		return absenceTimeList;
	}
	public void setAbsenceTimeList(List<AbsenceTimeDTO> absenceTimeList) {
		this.absenceTimeList = absenceTimeList;
	}
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
	public Double getOverTimeSum() {
		return overTimeSum;
	}
	public void setOverTimeSum(Double overTimeSum) {
		this.overTimeSum = overTimeSum;
	}
	public List<ExtDTO> getExts() {
		return exts;
	}
	public void setExts(List<ExtDTO> exts) {
		this.exts = exts;
	}
	public Byte getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(Byte userStatus) {
		this.userStatus = userStatus;
	}
	public String getPunchOrgName() {
		return punchOrgName;
	}
	public void setPunchOrgName(String punchOrgName) {
		this.punchOrgName = punchOrgName;
	}
	public String getPunchMonth() {
		return punchMonth;
	}
	public void setPunchMonth(String punchMonth) {
		this.punchMonth = punchMonth;
	}
}
