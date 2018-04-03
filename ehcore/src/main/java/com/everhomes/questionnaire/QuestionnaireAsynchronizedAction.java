// @formatter:off
package com.everhomes.questionnaire;


import com.everhomes.contentserver.ContentServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *  @author:dengs 2017年4月26日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuestionnaireAsynchronizedAction implements Runnable {
	@Autowired
	protected ContentServerService contentServerService;

	@Autowired
	private QuestionnaireAsynSendMessageService questionnaireAsynSendMessageService;

	private Long questionnaireId;

	public QuestionnaireAsynchronizedAction(final String questionnaireId) {
		this.questionnaireId = Long.valueOf(questionnaireId);
	}

	@Override
	public void run(){
		questionnaireAsynSendMessageService.sendAllTargetMessageAndSaveTargetScope(this.questionnaireId);
	}
}