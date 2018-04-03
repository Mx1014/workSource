package com.everhomes.rest.techpark.punch;

import java.sql.Date;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>id：</li>
 * <li>userId：用户id</li>
 * <li>userName：用户名称</li>
 * <li>userDepartment：部门</li>
 * <li>token：用户联系电话</li>
 * <li>companyId：公司id</li>
 * <li>punchDate: 打卡日期</li>
 * <li>arriveTime：上班打卡时间</li>
 * <li>leaveTime：下班打卡时间</li>
 * <li>workTime：工作时间</li>
 * <li>status：打卡状态</li>
 * <li>creatorUid：创建人id</li>
 * <li>createTime：创建时间</li>
 * <li>operatorName：审核人名称</li>
 * <li>approvalStatus：审批状态</li>
 * </ul>
 */
public class PunchStatisticsDTO{
	private Long     id;
	private Long     userId;
	private String     userName;
	private String userEnterpriseGroup;
	private String userDepartment;
	private String     userPhoneNumber;
	private Long     enterpriseId;
	private Long     punchDate;
	private Long      arriveTime;
	private Long     noonLeaveTime;
	private Long      afternoonArriveTime;
	private Long      leaveTime;
	private Long      workTime;
	private Byte     status;
	private java.lang.Byte     morningStatus;
	private java.lang.Byte     afternoonStatus;
	private Long     creatorUid;
	private Timestamp createTime;
	private String     operatorName;
	private Byte     approvalStatus;
	private java.lang.Byte     morningApprovalStatus;
	private java.lang.Byte     afternoonApprovalStatus;
	private java.lang.Byte     viewFlag;
	private java.lang.Byte     punchTimesPerDay;

	 private String statusList;
	
	
	
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


	public Long getCreatorUid() {
		return creatorUid;
	}


	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}


	public Timestamp getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}


	public Byte getApprovalStatus() {
		return approvalStatus;
	}


	public void setApprovalStatus(Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}


	public String getOperatorName() {
		return operatorName;
	}


	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public String getUserDepartment() {
		return userDepartment;
	}


	public void setUserDepartment(String userDepartment) {
		this.userDepartment = userDepartment;
	}


	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}


	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}


	public Long getEnterpriseId() {
		return enterpriseId;
	}


	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}


	public String getUserEnterpriseGroup() {
		return userEnterpriseGroup;
	}


	public void setUserEnterpriseGroup(String userEnterpriseGroup) {
		this.userEnterpriseGroup = userEnterpriseGroup;
	}


	 public String getStatusList() {
		 return statusList;
	 }

	 public void setStatusList(String statusList) {
		 this.statusList = statusList;
	 }
 }
