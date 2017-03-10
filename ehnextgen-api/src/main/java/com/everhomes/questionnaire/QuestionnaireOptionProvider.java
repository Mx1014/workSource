// @formatter:off
package com.everhomes.questionnaire;

import java.util.List;

public interface QuestionnaireOptionProvider {

	void createQuestionnaireOption(QuestionnaireOption questionnaireOption);

	void updateQuestionnaireOption(QuestionnaireOption questionnaireOption);

	QuestionnaireOption findQuestionnaireOptionById(Long id);

	List<QuestionnaireOption> listQuestionnaireOption();

	void deleteOptionsByQuestionnaireId(Long questionnaireId);

	List<QuestionnaireOption> listOptionsByQuestionId(Long questionId);

	List<QuestionnaireOption> listOptionsByQuestionnaireId(Long questionnaireId);

}