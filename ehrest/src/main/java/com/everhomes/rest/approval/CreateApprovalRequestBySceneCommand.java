// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 参数：
 * <li>sceneToken: 场景</li>
 * <li>approvalType: 审批类型，参考{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>categoryId: 审批类别id</li>
 * <li>reason: 申请理由</li>
 * <li>timeRangeList: 时间列表，参考{@link com.everhomes.rest.approval.TimeRange}</li>
 * <li>attachmentList: 附件列表，参考{@link com.everhomes.rest.news.AttachmentDescriptor}</li>
 * <li>contentJson: 内容json，请假暂无，异常申请参考{@link com.everhomes.rest.approval.ApprovalExceptionContent}</li>
 * <li>effectiveDate: 申请的生效日期(作用日期)</li>
 * <li>hourLength: 时长(多少个小时)</li>
 * </ul>
 */
public class CreateApprovalRequestBySceneCommand {
	private String sceneToken;
	private Byte approvalType;
	private Long categoryId;
	private String reason;
	@ItemType(TimeRange.class)
	private List<TimeRange> timeRangeList;
	@ItemType(AttachmentDescriptor.class)
	private List<AttachmentDescriptor> attachmentList;
	private String contentJson;
    private Long effectiveDate;
    private Double hourLength;
	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
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

	public List<AttachmentDescriptor> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<AttachmentDescriptor> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public String getContentJson() {
		return contentJson;
	}

	public void setContentJson(String contentJson) {
		this.contentJson = contentJson;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Long effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Double getHourLength() {
		return hourLength;
	}

	public void setHourLength(Double hourLength) {
		this.hourLength = hourLength;
	}
}
