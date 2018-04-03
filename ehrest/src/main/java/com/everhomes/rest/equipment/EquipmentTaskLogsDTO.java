package com.everhomes.rest.equipment;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>taskId: 任务id</li>
 *  <li>operatorType: 操作者类型</li>
 *  <li>operatorId: 操作人id</li>
 *  <li>operatorName: 操作人名称</li>
 *  <li>targetType: 目标类型（不一定有）</li>
 *  <li>targetId: 目标id（不一定有）</li>
 *  <li>targetName: 目标名字（不一定有）</li>
 *  <li>operateTime: 操作时间</li>
 *  <li>processType: 操作类型 参考{@link com.everhomes.rest.equipment.EquipmentTaskProcessType}</li>
 *  <li>processEndTime: 操作截止时间</li>
 *  <li>processResult: 操作结果 参考{@link com.everhomes.rest.equipment.EquipmentTaskProcessResult}</li>
 *  <li>processMessage: 操作内容</li>
 *  <li>templateId: 模板id</li>
 *  <li>templateName: 模板名称</li>
 *  <li>itemResults: 设备参数 参考{@link com.everhomes.rest.equipment.InspectionItemResult}</li>
 *  <li>createTime: 创建时间</li>
 *  <li>attachments: 附件， 参考{@link com.everhomes.rest.equipment.EquipmentTaskAttachmentDTO}</li>
 *  <li>reviewResult: 对巡检完成、维修完成、需维修三种需要审阅的记录补充审阅记录 参考{@link com.everhomes.rest.equipment.ReviewResult}</li>
 * </ul>
 */
public class EquipmentTaskLogsDTO {
	
	private Long id;
	
	private Long taskId;
	
	private String operatorType;

	private Long operatorId;
	
	private String operatorName;
	
	private String targetType;
	
	private Long targetId;
	
	private String targetName;
	
	private Timestamp operateTime;
	
	private Byte processType;
	
	private Timestamp processEndTime;
	
	private Byte processResult;
	
	private String processMessage;
	
	private Long templateId;
	
	private String templateName;

	@ItemType(InspectionItemResult.class)
    private List<InspectionItemResult> itemResults; 
	
	private Timestamp createTime;
	
	@ItemType(EquipmentTaskAttachmentDTO.class)
    private List<EquipmentTaskAttachmentDTO> attachments;
	
	private Byte reviewResult;
	
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

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
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

	public Timestamp getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
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

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<InspectionItemResult> getItemResults() {
		return itemResults;
	}

	public void setItemResults(List<InspectionItemResult> itemResults) {
		this.itemResults = itemResults;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<EquipmentTaskAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<EquipmentTaskAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public Byte getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(Byte reviewResult) {
		this.reviewResult = reviewResult;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
