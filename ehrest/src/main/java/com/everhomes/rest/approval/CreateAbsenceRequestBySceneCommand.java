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
 * <li>categoryId: 请假类型id</li>
 * <li>reason: 请假理由</li>
 * <li>timeRangeList: 时间列表，参考{@link com.everhomes.rest.approval.TimeRange}</li>
 * <li>attachmentList: 附件列表，参考{@link com.everhomes.rest.news.AttachmentDescriptor}</li>
 * </ul>
 */
public class CreateAbsenceRequestBySceneCommand {
	private String sceneToken;
	private Long categoryId;
	private String reason;
	@ItemType(TimeRange.class)
	private List<TimeRange> timeRangeList;
	@ItemType(AttachmentDescriptor.class)
	private List<AttachmentDescriptor> attachmentList;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
