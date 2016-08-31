// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>targetType: 目标类型，参考{@link com.everhomes.rest.approval.ApprovalTargetType}</li>
 * <li>targetId: 目标id</li>
 * <li>targetId: 目标名称</li>
 * </ul>
 */
public class ApprovalPerson {
	private Byte targetType;
	private Long targetId;
	private String targetName;

	public Byte getTargetType() {
		return targetType;
	}

	public void setTargetType(Byte targetType) {
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
