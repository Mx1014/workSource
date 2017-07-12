// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>questionnaire: 问卷调查，参考{@link com.everhomes.rest.questionnaire.QuestionnaireDTO}</li>
 * </ul>
 */
public class CreateQuestionnaireCommand {

	private QuestionnaireDTO questionnaire;

	public CreateQuestionnaireCommand() {

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
