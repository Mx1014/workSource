// @formatter:off
package com.everhomes.rest.approval;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>requestId: 申请ID</li>
 * <li>punchDate: 打卡日期</li>
 * <li>exceptionRequestType: 异常申请类型，参考{@link com.everhomes.rest.approval.ExceptionRequestType}</li>
 * <li>nickName: 姓名</li>
 * <li>reason: 申请理由</li>
 * <li>punchStatusName: 打卡状态</li>
 * <li>beginTime: 申请开始时间</li> 
 * <li>endTime: 申请结束时间</li> 
 * <li>duration: 申请时长</li> 
 * <li>flowCaseId: 申请工作流id</li> 
 * <li>approvalAttribute: 申请类型 参考{@link com.everhomes.rest.general_approval.GeneralApprovalAttribute}</li> 
 * <li>status: 申请状态 参考{@link com.everhomes.rest.approval.ApprovalStatus}</li> 
 * <li>requestTitle: 申请的标题(事假/病假/加班等等)</li> 
 * </ul>
 */
public class ExceptionRequestDTO {
	private Long requestId;
	private Timestamp punchDate;
	private Byte exceptionRequestType;
	private String nickName;
	private String reason;
	private String punchStatusName;
    private Long beginTime;
    private Long endTime;
    private Double duration;
    private Long flowCaseId;
    private String approvalAttribute; 
    private Byte status; 
    private String requestTitle; 
	
	public Byte getExceptionRequestType() {
		return exceptionRequestType;
	}

	public void setExceptionRequestType(Byte exceptionRequestType) {
		this.exceptionRequestType = exceptionRequestType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Timestamp getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Timestamp punchDate) {
		this.punchDate = punchDate;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPunchStatusName() {
		return punchStatusName;
	}

	public void setPunchStatusName(String punchStatusName) {
		this.punchStatusName = punchStatusName;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public String getApprovalAttribute() {
		return approvalAttribute;
	}

	public void setApprovalAttribute(String approvalAttribute) {
		this.approvalAttribute = approvalAttribute;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getRequestTitle() {
		return requestTitle;
	}

	public void setRequestTitle(String requestTitle) {
		this.requestTitle = requestTitle;
	}
}
