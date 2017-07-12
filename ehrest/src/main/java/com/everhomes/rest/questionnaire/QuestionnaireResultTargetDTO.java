// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>targetType: 目标类型</li>
 * <li>targetId: 目标id</li>
 * <li>targetName: 目标名称</li>
 * <li>submitTime: 提交时间</li>
 * </ul>
 */
public class QuestionnaireResultTargetDTO {
	private String targetType;
	private Long targetId;
	private String targetName;
	private Long submitTime;

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
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

	public Long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Long submitTime) {
		this.submitTime = submitTime;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
