package com.everhomes.rest.pmtask;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 任务log Id</li>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>taskId: 任务id</li>
 * <li>content: 内容</li>
 * <li>status: 状态 1: 未处理  2: 处理中 3: 已完成  4: 已关闭{@link com.everhomes.rest.pmtask.PmTaskStatus}</li>
 * <li>targetType: 目标类型</li>
 * <li>targetId: 目标id</li>
 * <li>operatorUid: 操作人id</li>
 * <li>operatorTime: 操作时间</li>
 * <li>text: 模版内容</li>
 * </ul>
 */
public class PmTaskLogDTO {
	private Long id;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long taskId;
	private String content;
	private Byte status;
	private String targetType;
	private Long targetId;
	private Long operatorUid;
	private String operatorName;
	private String operatorPhone;
	private Timestamp operatorTime;
	private String text;
	
	private String statusName;
	
	@ItemType(PmTaskAttachmentDTO.class)
	private List<PmTaskAttachmentDTO> attachments;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public Long getOperatorUid() {
		return operatorUid;
	}
	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}
	public Timestamp getOperatorTime() {
		return operatorTime;
	}
	public void setOperatorTime(Timestamp operatorTime) {
		this.operatorTime = operatorTime;
	}
	
	public List<PmTaskAttachmentDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<PmTaskAttachmentDTO> attachments) {
		this.attachments = attachments;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getOperatorPhone() {
		return operatorPhone;
	}
	public void setOperatorPhone(String operatorPhone) {
		this.operatorPhone = operatorPhone;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
}
