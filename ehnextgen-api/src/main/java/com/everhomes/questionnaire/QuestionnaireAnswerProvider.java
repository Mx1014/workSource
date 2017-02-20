// @formatter:off
package com.everhomes.questionnaire;

import java.util.List;

public interface QuestionnaireAnswerProvider {

	void createQuestionnaireAnswer(QuestionnaireAnswer questionnaireAnswer);

	void updateQuestionnaireAnswer(QuestionnaireAnswer questionnaireAnswer);

	QuestionnaireAnswer findQuestionnaireAnswerById(Long id);

	List<QuestionnaireAnswer> listQuestionnaireAnswer();

	List<QuestionnaireAnswer> listQuestionnaireTarget(Long questionnaireId, String keywords, int pageAnchor, int pageSize);

	List<QuestionnaireAnswer> listQuestionnaireAnswerByOptionId(Long optionId, Long pageAnchor, Integer pageSize);

	List<QuestionnaireAnswer> listQuestionnaireAnswerByQuestionId(Long questionId, Long pageAnchor, int pageSize);

	QuestionnaireAnswer findAnyAnswerByTarget(Long questionnaireId, String targetType, Long targetId);

	List<QuestionnaireAnswer> listTargetQuestionnaireAnswerByQuestionId(Long questionId, String targetType, Long targetId);

}