package com.everhomes.techpark.punch;

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
 * <li>requestType：申请类型申请或审批{@link com.everhomes.techpark.punch.PunchRquestType}</li>
 * <li>description：申请内容</li>
 * <li>status：异常处理状态：驳回，待审核，有效{@link com.everhomes.techpark.punch.ExceptionProcessStatus}</li>
 * <li>processCode：处理结果-审批考勤:正常，迟到等 参考{@link com.everhomes.techpark.punch.ApprovalStatus}</li>
 * <li>processDetails: 无效的时候有detail</li>
 * <li>creatorUid：创建人id</li>
 * <li>createTime：创建时间</li>
 * <li>operatorUid：审核人id</li>
 * <li>operatorName：审核人名称</li>
 * <li>operateTime：审核时间</li>
 * <li>punchStatus：打卡考勤:正常，迟到等 参考{@link com.everhomes.techpark.punch.PunchStatus}</li>
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
	private Long     companyId;
	private Date     punchDate;
	private Byte     requestType;
	private String   description;
	private Byte     status;
	private Byte     processCode;
	private String   processDetails;
	private Long     creatorUid;
	private Timestamp createTime;
	private Long     operatorUid;
	private String     operatorName;
	private Timestamp operateTime;
	private Byte punchStatus;
	private String arriveTime;
	private String leaveTime;
	private String workTime;
 
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


	public Long getCompanyId() {
		return companyId;
	}


	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}


	public Date getPunchDate() {
		return punchDate;
	}


	public void setPunchDate(Date punchDate) {
		this.punchDate = punchDate;
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


	public String getArriveTime() {
		return arriveTime;
	}


	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}


	public String getLeaveTime() {
		return leaveTime;
	}


	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}


	public String getWorkTime() {
		return workTime;
	}


	public void setWorkTime(String workTime) {
		this.workTime = workTime;
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

 

 }
