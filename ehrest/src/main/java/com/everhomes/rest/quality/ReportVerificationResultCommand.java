package com.everhomes.rest.quality;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>taskId: 任务id</li>
 *  <li>attachments: 核查上报内容图片</li>
 *  <li>message: 核查上报内容文字</li>
 *  <li>verificationResult: 核查结果  参考{@link com.everhomes.rest.quality.QualityInspectionTaskResult}</li>
 *  <li>endTime: 整改截止时间</li>
 *  <li>operatorType: 整改执行人类型</li>
 *  <li>operatorId: 整改执行人id</li>
 *  <li>itemResults: 规范事项  参考{@link com.everhomes.rest.quality.ReportSpecificationItemResultsDTO}</li>
 * </ul>
 */
public class ReportVerificationResultCommand {
	
	@NotNull
	private Long taskId;
	
	@NotNull
	private Byte verificationResult;
	
	@NotNull
	@ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;
	
	private Long endTime;
	
	private String operatorType;
	
	private Long operatorId;
	
	private String message;
	
	@ItemType(ReportSpecificationItemResultsDTO.class)
	private List<ReportSpecificationItemResultsDTO> itemResults;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Byte getVerificationResult() {
		return verificationResult;
	}

	public void setVerificationResult(Byte verificationResult) {
		this.verificationResult = verificationResult;
	}

	public List<AttachmentDescriptor> getAttachments() {
		return attachments;
	}
	
	public void setAttachments(List<AttachmentDescriptor> attachments) {
		this.attachments = attachments;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public List<ReportSpecificationItemResultsDTO> getItemResults() {
		return itemResults;
	}

	public void setItemResults(List<ReportSpecificationItemResultsDTO> itemResults) {
		this.itemResults = itemResults;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
