// @formatter:off
package com.everhomes.rest.approval;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>requestToken: 申请的token</li>
 * <li>approveType: 审批类型，{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>categoryName: 具体类型，异常无，请假申请时公出、事假等</li>
 * <li>title: 申请title内容比如谁申请,反馈等</li>
 * <li>reason: 申请理由</li>
 * <li>description: 描述，请假总时长等</li>
 * <li>approvalStatus: 审批状态，参考{@link com.everhomes.rest.approval.ApprovalStatus}</li>
 * <li>createTime: 创建时间</li>
 * <li>timeRangeList: 时间列表，参考{@link com.everhomes.rest.approval.TimeRange}</li>
 * <li>attachmentList: 时间列表，参考{@link com.everhomes.rest.news.AttachmentDescriptor}</li>
 * </ul>
 */
public class BriefApprovalRequestDTO {
	private String requestToken;
	private Byte approvalType;
	private String categoryName;
	private String description;
	@ItemType(TimeRange.class)
	private List<TimeRange> timeRangeList;
	private String title;
	private String reason;
	private Byte approvalStatus;
	private Timestamp createTime;
	@ItemType(AttachmentDescriptor.class)
	private List<AttachmentDescriptor> attachmentList;

	public List<AttachmentDescriptor> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<AttachmentDescriptor> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Byte getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(Byte approvalType) {
		this.approvalType = approvalType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<TimeRange> getTimeRangeList() {
		return timeRangeList;
	}

	public void setTimeRangeList(List<TimeRange> timeRangeList) {
		this.timeRangeList = timeRangeList;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
