// @formatter:off
package com.everhomes.questionnaire;

import java.util.List;

public interface QuestionnaireProvider {

	void createQuestionnaire(Questionnaire questionnaire);

	void updateQuestionnaire(Questionnaire questionnaire);

	Questionnaire findQuestionnaireById(Long id);

	List<Questionnaire> listQuestionnaire();

}