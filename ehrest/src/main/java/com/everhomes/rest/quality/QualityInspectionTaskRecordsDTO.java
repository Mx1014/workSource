package com.everhomes.rest.quality;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class QualityInspectionTaskRecordsDTO {
	
	private Long id;
	private Long taskId;
	private Long operatorId;
	private Long targetId;
	private Byte processType;
	private Timestamp processEndTime;
	private Byte processResult;
	private String processMessage;
	private Timestamp createTime;
	
	@ItemType(QualityInspectionTaskAttachmentDTO.class)
    private List<QualityInspectionTaskAttachmentDTO> attachments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Byte getProcessType() {
		return processType;
	}

	public void setProcessType(Byte processType) {
		this.processType = processType;
	}

	public Timestamp getProcessEndTime() {
		return processEndTime;
	}

	public void setProcessEndTime(Timestamp processEndTime) {
		this.processEndTime = processEndTime;
	}

	public Byte getProcessResult() {
		return processResult;
	}

	public void setProcessResult(Byte processResult) {
		this.processResult = processResult;
	}

	public String getProcessMessage() {
		return processMessage;
	}

	public void setProcessMessage(String processMessage) {
		this.processMessage = processMessage;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<QualityInspectionTaskAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<QualityInspectionTaskAttachmentDTO> attachments) {
		this.attachments = attachments;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
