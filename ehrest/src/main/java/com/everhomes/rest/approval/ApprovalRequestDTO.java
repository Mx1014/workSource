// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.approval.ApprovalOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>approvalType: 审批类型，参考{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>categoryId: 审批类别</li>
 * <li>contentJson: 内容json</li>
 * <li>attachmentFlag: 是否有附件，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>timeFlag: 是否有时间，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>flowId: 对应流程id</li>
 * <li>currentLevel: 当前进行到流程第几步</li>
 * <li>nextLevel: 流程下一步</li>
 * <li>approvalStatus: 审批状态，参考{@link com.everhomes.rest.approval.ApprovalStatus}</li>
 * <li>attachmentList: 附件列表，参考{@link com.everhomes.rest.news.AttachmentDescriptor}</li>
 * <li>timeRangeList: 时间列表，参考{@link com.everhomes.rest.approval.TimeRange}</li>
 * </ul>
 */
public class ApprovalRequestDTO {
	private Long id;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Byte approvalType;
	private Long categoryId;
	private String contentJson;
	private Byte attachmentFlag;
	private Byte timeFlag;
	private Long flowId;
	private Long currentLevel;
	private Long nextLevel;
	private Byte approvalStatus;
	@ItemType(AttachmentDescriptor.class)
	private List<AttachmentDescriptor> attachmentList;
	@ItemType(TimeRange.class)
	private List<TimeRange> timeRangeList;

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

	public Byte getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(Byte approvalType) {
		this.approvalType = approvalType;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getContentJson() {
		return contentJson;
	}

	public void setContentJson(String contentJson) {
		this.contentJson = contentJson;
	}

	public Byte getAttachmentFlag() {
		return attachmentFlag;
	}

	public void setAttachmentFlag(Byte attachmentFlag) {
		this.attachmentFlag = attachmentFlag;
	}

	public Byte getTimeFlag() {
		return timeFlag;
	}

	public void setTimeFlag(Byte timeFlag) {
		this.timeFlag = timeFlag;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public Long getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Long currentLevel) {
		this.currentLevel = currentLevel;
	}

	public Long getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(Long nextLevel) {
		this.nextLevel = nextLevel;
	}

	public Byte getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public List<AttachmentDescriptor> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<AttachmentDescriptor> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public List<TimeRange> getTimeRangeList() {
		return timeRangeList;
	}

	public void setTimeRangeList(List<TimeRange> timeRangeList) {
		this.timeRangeList = timeRangeList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
