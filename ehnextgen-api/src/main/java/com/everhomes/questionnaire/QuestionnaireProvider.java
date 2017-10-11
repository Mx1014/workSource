// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.rest.questionnaire.ListQuestionnairesCommand;

import java.util.List;

public interface QuestionnaireProvider {

	void createQuestionnaire(Questionnaire questionnaire);

	void updateQuestionnaire(Questionnaire questionnaire);

	Questionnaire findQuestionnaireById(Long id);

	List<Questionnaire> listQuestionnaire();

	List<Questionnaire> listQuestionnaireByOwner(ListQuestionnairesCommand cmd,Integer namespaceId,
												 int pageSize);

	List<Questionnaire> listTargetQuestionnaireByOwner(Integer namespaceId, String ownerType, Long ownerId,
			Long pageAnchor, int pageSize);

}