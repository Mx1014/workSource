package com.everhomes.techpark.punch;

import java.sql.Date;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>userId：申请人id</li>
 * <li>punchDate: 申请时间</li>
 * <li>status：异常处理状态：驳回，待审核，同意{@link com.everhomes.techpark.punch.ExceptionProcessStatus}</li>
 * <li>processCode：处理结果:正常，迟到等 参考{@link com.everhomes.techpark.punch.ApprovalStatus}</li>
 * <li>processDetails: 无效的时候有detail</li>
 * <li>creatorUid：创建人id</li>
 * <li>createTime：创建时间</li>
 * <li>operatorUid：审核人id</li>
 * <li>operateTime：审核时间</li>
 * </ul>
 */
public class ApprovalPunchExceptionCommand{
	private Long     userId;
	private Long     enterpriseId;
	private String     punchDate;
	private Byte     status;
	private Byte     processCode;
	private String   processDetails;
	private Long     creatorUid;
	private Long     operatorUid;
 
  


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
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
