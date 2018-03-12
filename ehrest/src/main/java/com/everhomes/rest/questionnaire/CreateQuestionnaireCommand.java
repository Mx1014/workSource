// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>questionnaire: 问卷调查，参考{@link com.everhomes.rest.questionnaire.QuestionnaireDTO}</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class CreateQuestionnaireCommand {

	private QuestionnaireDTO questionnaire;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public CreateQuestionnaireCommand() {

	}

	public QuestionnaireDTO getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(QuestionnaireDTO questionnaire) {
		this.questionnaire = questionnaire;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
