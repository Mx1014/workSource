// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>questionnaire: 问卷调查详情，参考{@link com.everhomes.rest.questionnaire.QuestionnaireDTO}</li>
 * </ul>
 */
public class GetOrganizationQuestionnaireDetailResponse {

	private QuestionnaireDTO questionnaire;

	public GetOrganizationQuestionnaireDetailResponse() {

	}

	public GetOrganizationQuestionnaireDetailResponse(QuestionnaireDTO questionnaire) {
		super();
		this.questionnaire = questionnaire;
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
