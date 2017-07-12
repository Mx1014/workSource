package com.everhomes.rest.quality;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: record主键id</li>
 *  <li>taskId: 任务id</li>
 *  <li>operatorId: 操作人id</li>
 *  <li>targetId: 目标id（不一定有）</li>
 *  <li>targetName: 目标名字（不一定有）</li>
 *  <li>processType: 操作类型 0: none, 1: inspect核查, 2: retify整改, 3: review审阅, 4: assgin分配, 5: forward转发</li>
 *  <li>processEndTime: 操作截止时间</li>
 *  <li>processResult: 操作结果</li>
 *  <li>processMessage: 操作内容</li>
 *  <li>createTime: 创建时间</li>
 *  <li>attachments: 附件， 参考{@link com.everhomes.rest.quality.QualityInspectionTaskAttachmentDTO}</li>
 *  <li>itemResults: 规范事项记录， 参考{@link com.everhomes.rest.quality.QualityInspectionSpecificationItemResultsDTO}</li>
 * </ul>
 */
public class QualityInspectionTaskRecordsDTO {
	
	private Long id;
	private Long taskId;
	private Long operatorId;
	private Long targetId;
	private String targetName;
	private Byte processType;
	private Timestamp processEndTime;
	private Byte processResult;
	private String processMessage;
	private Timestamp createTime;
	
	@ItemType(QualityInspectionTaskAttachmentDTO.class)
    private List<QualityInspectionTaskAttachmentDTO> attachments;

	@ItemType(QualityInspectionSpecificationItemResultsDTO.class)
	private List<QualityInspectionSpecificationItemResultsDTO> itemResults;
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

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
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
	
	public List<QualityInspectionSpecificationItemResultsDTO> getItemResults() {
		return itemResults;
	}

	public void setItemResults(
			List<QualityInspectionSpecificationItemResultsDTO> itemResults) {
		this.itemResults = itemResults;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
