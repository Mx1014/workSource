// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>type: 1表示日志，2表示审批流程</li>
 * <li>contentJson: 内容，日志请参考{@link com.everhomes.rest.approval.ApprovalLogOfRequestDTO}，审批流程请参考{@link com.everhomes.rest.approval.ApprovalFlowOfRequestDTO}</li>
 * </ul>
 */
public class ApprovalLogAndFlowOfRequestDTO {
	private Byte type;
	private String contentJson;

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
}
