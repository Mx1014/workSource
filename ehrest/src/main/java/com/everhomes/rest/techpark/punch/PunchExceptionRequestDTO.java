package com.everhomes.rest.techpark.punch;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>id：</li>
 * <li>userId：申请人id</li>
 * <li>userName：申请人名称</li>
 * <li>userPhoneNumber：申请人联系电话</li>
 * <li>companyId：公司id</li>
 * <li>punchDate: 申请时间</li>
 * <li>requestType：申请类型申请或审批{@link com.everhomes.rest.techpark.punch.PunchRquestType}</li>
 * <li>description：申请内容</li>
 * <li>status：异常处理状态：驳回，待审核，有效{@link com.everhomes.rest.techpark.punch.ExceptionProcessStatus}</li>
 * <li>processCode：处理结果-审批考勤:正常，迟到等 参考{@link com.everhomes.rest.techpark.punch.ApprovalStatus}</li>
 * <li>processDetails: 无效的时候有detail</li>
 * <li>creatorUid：创建人id</li>
 * <li>createTime：创建时间</li>
 * <li>operatorUid：审核人id</li>
 * <li>operatorName：审核人名称</li>
 * <li>operateTime：审核时间</li>
 * <li>punchStatus：打卡考勤:正常，迟到等 参考{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>arriveTime：上班时间</li>
 * <li>leaveTime：下班时间</li>
 * <li>workTime：工作时长</li>
 * </ul>
 */
public class PunchExceptionRequestDTO{
	private Long     id;
	private Long     userId;
	private String     userName;
	private String     userPhoneNumber;
	private Long     enterpriseId;
	private Long     punchDate;
	private Byte     requestType;
	private String   description;
	private Byte     status;

	private java.lang.Byte     approvalStatus;
	private java.lang.Byte     morningApprovalStatus;
	private java.lang.Byte     afternoonApprovalStatus;
	
	private Byte     processCode;
	private String   processDetails;
	private Long     creatorUid;
	private Timestamp createTime;
	private Long     operatorUid;
	private String     operatorName;
	private Timestamp operateTime;
	private Byte punchStatus;

	private java.lang.Byte     morningPunchStatus;
	private java.lang.Byte     afternoonPunchStatus;
	private Long arriveTime;
	private Long leaveTime;
	private Long workTime;
	private Long      noonLeaveTime;
	private Long    afternoonArriveTime;
	private java.lang.Byte     punchTimesPerDay;
 
    public java.lang.Byte getApprovalStatus() {
		return approvalStatus;
	}


	public void setApprovalStatus(java.lang.Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}


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


	public Long getEnterpriseId() {
		return enterpriseId;
	}


	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
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


 
 
	public Byte getRequestType() {
		return requestType;
	}


	public void setRequestType(Byte requestType) {
		this.requestType = requestType;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Byte getStatus() {
		return status;
	}


	public void setStatus(Byte status) {
		this.status = status;
	}


	public Byte getProcessCode() {
		return processCode;
	}


	public void setProcessCode(Byte processCode) {
		this.processCode = processCode;
	}


	public String getProcessDetails() {
		return processDetails;
	}


	public void setProcessDetails(String processDetails) {
		this.processDetails = processDetails;
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


	public Long getOperatorUid() {
		return operatorUid;
	}


	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}


	public Timestamp getOperateTime() {
		return operateTime;
	}


	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}

 


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public Byte getPunchStatus() {
		return punchStatus;
	}


	public void setPunchStatus(Byte punchStatus) {
		this.punchStatus = punchStatus;
	}
 
 


	public String getOperatorName() {
		return operatorName;
	}


	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}


	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}


	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}


	public java.lang.Byte getMorningPunchStatus() {
		return morningPunchStatus;
	}


	public void setMorningPunchStatus(java.lang.Byte morningPunchStatus) {
		this.morningPunchStatus = morningPunchStatus;
	}


	public java.lang.Byte getAfternoonPunchStatus() {
		return afternoonPunchStatus;
	}


	public void setAfternoonPunchStatus(java.lang.Byte afternoonPunchStatus) {
		this.afternoonPunchStatus = afternoonPunchStatus;
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

 

 }
