// @formatter:off
package com.everhomes.questionnaire;

public interface QuestionnaireAsynSendMessageService {

	public void sendAllTargetMessageAndSaveTargetScope(Long questionnaireId);

	public void sendUnAnsweredTargetMessage();
}