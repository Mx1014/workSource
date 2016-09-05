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
 * <li>punchDate: 打卡日期时间戳</li>
 * <li>forgetToPunchType: 忘打卡类型，{@link com.everhomes.rest.approval.ForgetToPunchType}</li>
 * <li>reason: 申请理由</li>
 * <li>attachmentList: 附件列表，参考{@link com.everhomes.rest.news.AttachmentDescriptor}</li>
 * </ul>
 */
public class CreateForgetToPunchRequestBySceneCommand {
	private String sceneToken;
	private Long punchDate;
	private Byte forgetToPunchType;
	private String reason;
	@ItemType(AttachmentDescriptor.class)
	private List<AttachmentDescriptor> attachmentList;

	public Byte getForgetToPunchType() {
		return forgetToPunchType;
	}

	public void setForgetToPunchType(Byte forgetToPunchType) {
		this.forgetToPunchType = forgetToPunchType;
	}

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

	public Long getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Long punchDate) {
		this.punchDate = punchDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
	}}
