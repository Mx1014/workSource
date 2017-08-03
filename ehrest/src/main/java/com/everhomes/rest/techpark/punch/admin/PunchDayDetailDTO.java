package com.everhomes.rest.techpark.punch.admin;

import java.sql.Date;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：</li>
 * <li>userId：用户id</li>
 * <li>userName：用户名称</li>
 * <li>deptName：部门</li>  
 * <li>punchOrgName：所属规则</li>  
 * <li>punchDate: 打卡日期</li>
 * <li>arriveTime：上班打卡时间</li>
 * <li>leaveTime：下班打卡时间</li>
 * <li>workTime：工作时间</li>
 * <li>punchCount：打卡次数</li>
 * <li>status：打卡状态{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>morningStatus：早上打卡状态{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>afternoonStatus：下午打卡状态{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>approvalStatus：考勤状态{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>morningApprovalStatus：上午考勤状态{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>afternoonApprovalStatus：下午考勤状态{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>exceptionStatus：状态{@link com.everhomes.rest.techpark.punch.ExceptionStatus}</li>
 * <li>punchTimesPerDay：打卡状态</li> 
 * <li>deviceChangeFlag：设备改变  0-没变 1-改变标红</li> 
 * <li>statuString：状态文字</li> 
 * </ul>
 */
public class PunchDayDetailDTO {
	private Long id;
	private Long userId;
	private String userName;
	private String deptName;   
	private String punchOrgName;   
	private Long punchDate;
	private Long arriveTime;
	private Long noonLeaveTime;
	private Long afternoonArriveTime;
	private Long leaveTime;
	private Long workTime;
	private Integer punchCount;
	private Byte status;
	private java.lang.Byte morningStatus;
	private java.lang.Byte afternoonStatus;  
	private Byte approvalStatus;
	private java.lang.Byte morningApprovalStatus;
	private java.lang.Byte afternoonApprovalStatus;
	private java.lang.Byte viewFlag;
	private java.lang.Byte punchTimesPerDay;
    private Byte exceptionStatus ;
    private Byte deviceChangeFlag;
    private String statuString;
	public java.lang.Byte getMorningApprovalStatus() {
		return morningApprovalStatus;
	}

	public void setMorningApprovalStatus(java.lang.Byte morningApprovalStatus) {
		this.morningApprovalStatus = morningApprovalStatus;
	}

	public java.lang.Byte getAfternoonApprovalStatus() {
		return afternoonApprovalStatus;
	}

	public void setAfternoonApprovalStatus(java.lang.Byte afternoonApprovalStatus) {
		this.afternoonApprovalStatus = afternoonApprovalStatus;
	}

	public java.lang.Byte getMorningStatus() {
		return morningStatus;
	}

	public void setMorningStatus(java.lang.Byte morningStatus) {
		this.morningStatus = morningStatus;
	}

	public java.lang.Byte getAfternoonStatus() {
		return afternoonStatus;
	}

	public void setAfternoonStatus(java.lang.Byte afternoonStatus) {
		this.afternoonStatus = afternoonStatus;
	}

	public java.lang.Byte getViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(java.lang.Byte viewFlag) {
		this.viewFlag = viewFlag;
	}

	public java.lang.Byte getPunchTimesPerDay() {
		return punchTimesPerDay;
	}

	public void setPunchTimesPerDay(java.lang.Byte punchTimesPerDay) {
		this.punchTimesPerDay = punchTimesPerDay;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Long punchDate) {
		this.punchDate = punchDate;
	}

	public Long getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(Long arriveTime) {
		this.arriveTime = arriveTime;
	}

	public Long getNoonLeaveTime() {
		return noonLeaveTime;
	}

	public void setNoonLeaveTime(Long noonLeaveTime) {
		this.noonLeaveTime = noonLeaveTime;
	}

	public Long getAfternoonArriveTime() {
		return afternoonArriveTime;
	}

	public void setAfternoonArriveTime(Long afternoonArriveTime) {
		this.afternoonArriveTime = afternoonArriveTime;
	}

	public Long getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(Long leaveTime) {
		this.leaveTime = leaveTime;
	}

	public Long getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Long workTime) {
		this.workTime = workTime;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
 

	public Byte getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
 

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Byte getExceptionStatus() {
		return exceptionStatus;
	}

	public void setExceptionStatus(Byte exceptionStatus) {
		this.exceptionStatus = exceptionStatus;
	}

	public Byte getDeviceChangeFlag() {
		return deviceChangeFlag;
	}

	public void setDeviceChangeFlag(Byte deviceChangeFlag) {
		this.deviceChangeFlag = deviceChangeFlag;
	}

	public String getStatuString() {
		return statuString;
	}

	public void setStatuString(String statuString) {
		this.statuString = statuString;
	}

	public String getPunchOrgName() {
		return punchOrgName;
	}

	public void setPunchOrgName(String punchOrgName) {
		this.punchOrgName = punchOrgName;
	}

	public Integer getPunchCount() {
		return punchCount;
	}

	public void setPunchCount(Integer punchCount) {
		this.punchCount = punchCount;
	}
 

}
