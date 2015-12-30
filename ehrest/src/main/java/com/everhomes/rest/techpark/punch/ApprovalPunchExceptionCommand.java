package com.everhomes.rest.techpark.punch;

import java.sql.Date;
import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>userId：申请人id</li>
 * <li>punchDate: 申请时间</li>
 * <li>status：异常处理状态：驳回，待审核，同意{@link com.everhomes.rest.techpark.punch.ExceptionProcessStatus}</li>
 * <li>processCode：处理结果:正常，迟到等 参考{@link com.everhomes.rest.techpark.punch.ApprovalStatus}</li>
 * <li>processDetails: 无效的时候有detail</li>
 * <li>creatorUid：创建人id</li>
 * <li>createTime：创建时间</li>
 * <li>operatorUid：审核人id</li>
 * <li>operateTime：审核时间</li>
 * </ul>
 */
public class ApprovalPunchExceptionCommand{
	@NotNull
	private Long     userId;
	@NotNull
	private Long     enterpriseId;
	@NotNull
	private String     punchDate;
	@NotNull
	private Byte    status;
	private java.lang.Byte     morningApprovalStatus;
	private java.lang.Byte     afternoonApprovalStatus;
	private Byte     approvalStatus;
	@NotNull
	private String   processDetails;
	private Long     creatorUid;
	private Long     operatorUid;
	@NotNull
	private java.lang.Byte     punchTimesPerDay;
 
  


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}

 
 
 

 

	public Byte getApprovalStatus() {
		return approvalStatus;
	}


	public void setApprovalStatus(Byte approvalStatus) {
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


	public java.lang.Byte getPunchTimesPerDay() {
		return punchTimesPerDay;
	}


	public void setPunchTimesPerDay(java.lang.Byte punchTimesPerDay) {
		this.punchTimesPerDay = punchTimesPerDay;
	}
 

	public Byte getStatus() {
		return status;
	}


	public void setStatus(Byte status) {
		this.status = status;
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
 

	public Long getOperatorUid() {
		return operatorUid;
	}


	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}


	 


	 


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public String getPunchDate() {
		return punchDate;
	}


	public void setPunchDate(String punchDate) {
		this.punchDate = punchDate;
	}


	public Long getEnterpriseId() {
		return enterpriseId;
	}


	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

 }
