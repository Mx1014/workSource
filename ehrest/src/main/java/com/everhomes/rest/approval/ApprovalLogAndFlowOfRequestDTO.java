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
 * <li>type: 1表示日志，2表示审批流程</li>
 * <li>contentJson: 内容，日志请参考{@link com.everhomes.rest.approval.ApprovalLogOfRequestDTO}，审批流程请参考{@link com.everhomes.rest.approval.ApprovalFlowOfRequestDTO}</li>
 * 
 * <li>createTime: 申请时间/审批时间</li> 
 * <li>approvalType: 审批类型1-请假 2-异常 3-加班，参考{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>categoryName: 申请事项，异常申请无，请假时为具体类型如公出、事假等</li>
 * <li>attachmentList: 附件列表，参考{@link com.everhomes.rest.news.AttachmentDescriptor}</li>
 * <li>approvalStatus: 审批状态0-待审批 1-审批 2-驳回，参考{@link com.everhomes.rest.approval.ApprovalStatus}</li>
 * <li>title: 申请title内容比如谁申请,反馈等</li>
 * <li>remark: 申请body内容 比如 反馈信息</li>
 * <li>currentFlag: 是否为当前节点，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ApprovalLogAndFlowOfRequestDTO {
	private Byte type;
	private String contentJson;
	private Timestamp createTime; 
	private Byte approvalType;
	private String categoryName;
	private Byte approvalStatus;
	private String title;
	private String remark;
	@ItemType(AttachmentDescriptor.class)
	private List<AttachmentDescriptor> attachmentList;
	private Byte currentFlag;

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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Byte getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
 

	public List<AttachmentDescriptor> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<AttachmentDescriptor> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Byte getCurrentFlag() {
		return currentFlag;
	}

	public void setCurrentFlag(Byte currentFlag) {
		this.currentFlag = currentFlag;
	}
}
