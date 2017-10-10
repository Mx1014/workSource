// @formatter:off
package com.everhomes.questionnaire;

import java.util.List;

public interface QuestionnaireRangeProvider {

	void createQuestionnaireRange(QuestionnaireRange questionnaireRange);

	void updateQuestionnaireRange(QuestionnaireRange questionnaireRange);

	QuestionnaireRange findQuestionnaireRangeById(Long id);

	List<QuestionnaireRange> listQuestionnaireRange();

}