// @formatter:off
package com.everhomes.rest.approval;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>createTime: 申请时间/审批时间</li>
 * <li>avatarUrl: 头像URL</li>
 * <li>nickName: 用户/审批人姓名</li>
 * <li>categoryName: 具体类型名称，忘打卡申请时为忘打卡，请假申请时为公出、事假等</li>
 * <li>attachmentList: 附件列表，参考{@link com.everhomes.rest.news.AttachmentDescriptor}</li>
 * <li>approvalStatus: 审批状态，参考{@link com.everhomes.rest.approval.ApprovalStatus}</li>
 * <li>remark: 申请理由/反馈</li>
 * </ul>
 */
public class ApprovalLogOfRequestDTO {
	private Timestamp createTime;
	private String avatarUrl;
	private String nickName;
	private String categoryName;
	private List<AttachmentDescriptor> attachmentList;
	private Byte approvalStatus;
	private String remark;

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<AttachmentDescriptor> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<AttachmentDescriptor> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public Byte getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
