package com.everhomes.techpark.punch;

import java.sql.Date;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>id：</li>
 * <li>userId：申请人id</li>
 * <li>userName：申请人名称</li>
 * <li>token：申请人联系电话</li>
 * <li>companyId：公司id</li>
 * <li>punchDate: 打卡日期</li>
 * <li>arriveTime：上班打卡时间</li>
 * <li>leaveTime：下班打卡时间</li>
 * <li>workTime：工作时间</li>
 * <li>status：打卡状态</li>
 * <li>creatorUid：创建人id</li>
 * <li>createTime：创建时间</li>
 * <li>approvalStatus：审批状态</li>
 * </ul>
 */
public class PunchStatisticsDTO{
	private Long     id;
	private Long     userId;
	private String     userName;
	private String     token;
	private Long     companyId;
	private Date     punchDate;
	private String      arriveTime;
	private String      leaveTime;
	private String      workTime;
	private Byte     status;
	private Long     creatorUid;
	private Timestamp createTime;
	
	private Byte     approvalStatus;
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


	
	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
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


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



 }
