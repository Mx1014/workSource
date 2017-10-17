// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.rest.questionnaire.ListQuestionnairesCommand;
import com.everhomes.rest.questionnaire.QuestionnaireDTO;

import java.sql.Timestamp;
import java.util.List;

public interface QuestionnaireProvider {

	void createQuestionnaire(Questionnaire questionnaire);

	void updateQuestionnaire(Questionnaire questionnaire);

	Questionnaire findQuestionnaireById(Long id);

	List<Questionnaire> listQuestionnaire();

	List<Questionnaire> listQuestionnaireByOwner(ListQuestionnairesCommand cmd,Integer namespaceId,
												 int pageSize);

	List<QuestionnaireDTO> listTargetQuestionnaireByOwner(Integer namespaceId, Timestamp nowTime, Byte collectFlag, Long UserId,
														  Byte answerFlagAnchor, Long publishTimeAnchor, int pageSize);

}