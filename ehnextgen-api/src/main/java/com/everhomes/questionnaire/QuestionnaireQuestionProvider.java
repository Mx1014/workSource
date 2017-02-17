// @formatter:off
package com.everhomes.questionnaire;

import java.util.List;

public interface QuestionnaireQuestionProvider {

	void createQuestionnaireQuestion(QuestionnaireQuestion questionnaireQuestion);

	void updateQuestionnaireQuestion(QuestionnaireQuestion questionnaireQuestion);

	QuestionnaireQuestion findQuestionnaireQuestionById(Long id);

	List<QuestionnaireQuestion> listQuestionnaireQuestion();

	void deleteQuestionsByQuestionnaireId(Long questionnaireId);

	List<QuestionnaireQuestion> listQuestionsByQuestionnaireId(Long questionnaireId);

}