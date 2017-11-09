// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>targetType: 目标类型, 参考{@link com.everhomes.rest.questionnaire.QuestionnaireTargetType}</li>
 * <li>targetId: 目标id</li>
 * <li>targetName: 目标名称</li>
 * <li>targetFrom: 目标类型为user时候，用户来源，参考 {@link com.everhomes.rest.questionnaire.QuestionnaireUserType}</li>
 * <li>targetPhone: 目标类型为user时候,用户的电话</li>
 * <li>submitTime: 提交时间</li>
 * <li>anonymousFlag: 是否匿名回答，参考 {@link com.everhomes.rest.questionnaire.QuestionnaireCommonStatus}</li>
 * </ul>
 */
public class QuestionnaireResultTargetDTO {
	private String targetType;
	private Long targetId;
	private String targetName;
	private Byte targetFrom;
	private String targetPhone;
	private Long submitTime;
	private Byte anonymousFlag;

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

	public Byte getTargetFrom() {
		return targetFrom;
	}

	public void setTargetFrom(Byte targetFrom) {
		this.targetFrom = targetFrom;
	}

	public String getTargetPhone() {
		return targetPhone;
	}

	public void setTargetPhone(String targetPhone) {
		this.targetPhone = targetPhone;
	}

	public Long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Long submitTime) {
		this.submitTime = submitTime;
	}

	public Byte getAnonymousFlag() {
		return anonymousFlag;
	}

	public void setAnonymousFlag(Byte anonymousFlag) {
		this.anonymousFlag = anonymousFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
