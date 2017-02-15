// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>targetType: 目标类型，organization</li>
 * <li>targetId: 目标id，organization</li>
 * <li>questionnaire: 问卷调查详情，参考{@link com.everhomes.rest.questionnaire.QuestionnaireDTO}</li>
 * </ul>
 */
public class CreateTargetQuestionnaireCommand {

	private String targetType;

	private Long targetId;

	private QuestionnaireDTO questionnaire;

	public CreateTargetQuestionnaireCommand() {

	}

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

	public QuestionnaireDTO getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(QuestionnaireDTO questionnaire) {
		this.questionnaire = questionnaire;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
