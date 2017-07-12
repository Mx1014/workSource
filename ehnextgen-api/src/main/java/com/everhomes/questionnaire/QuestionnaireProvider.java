// @formatter:off
package com.everhomes.questionnaire;

import java.util.List;

public interface QuestionnaireProvider {

	void createQuestionnaire(Questionnaire questionnaire);

	void updateQuestionnaire(Questionnaire questionnaire);

	Questionnaire findQuestionnaireById(Long id);

	List<Questionnaire> listQuestionnaire();

	List<Questionnaire> listQuestionnaireByOwner(Integer namespaceId, String ownerType, Long ownerId, Long pageAnchor,
			int pageSize);

	List<Questionnaire> listTargetQuestionnaireByOwner(Integer namespaceId, String ownerType, Long ownerId,
			Long pageAnchor, int pageSize);

}