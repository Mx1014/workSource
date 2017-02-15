// @formatter:off
package com.everhomes.questionnaire;

import java.util.List;

public interface QuestionnaireAnswerProvider {

	void createQuestionnaireAnswer(QuestionnaireAnswer questionnaireAnswer);

	void updateQuestionnaireAnswer(QuestionnaireAnswer questionnaireAnswer);

	QuestionnaireAnswer findQuestionnaireAnswerById(Long id);

	List<QuestionnaireAnswer> listQuestionnaireAnswer();

}