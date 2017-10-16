// @formatter:off
package com.everhomes.rest.questionnaire;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>answerTimeAnchor: 回答时间的锚点</li>
 * <li>publishTimeAnchor: 发布时间的锚点</li>
 * <li>questionnaires: 问卷调查列表，参考{@link com.everhomes.rest.questionnaire.QuestionnaireDTO}</li>
 * </ul>
 */
public class ListTargetQuestionnairesResponse {

	private Long answerTimeAnchor;

	private Long publishTimeAnchor;

	@ItemType(QuestionnaireDTO.class)
	private List<QuestionnaireDTO> questionnaires;

	public ListTargetQuestionnairesResponse() {

	}

	public ListTargetQuestionnairesResponse(Long answerTimeAnchor, Long publishTimeAnchor, List<QuestionnaireDTO> questionnaires) {
		this.answerTimeAnchor = answerTimeAnchor;
		this.publishTimeAnchor = publishTimeAnchor;
		this.questionnaires = questionnaires;
	}

	public Long getAnswerTimeAnchor() {
		return answerTimeAnchor;
	}

	public void setAnswerTimeAnchor(Long answerTimeAnchor) {
		this.answerTimeAnchor = answerTimeAnchor;
	}

	public Long getPublishTimeAnchor() {
		return publishTimeAnchor;
	}

	public void setPublishTimeAnchor(Long publishTimeAnchor) {
		this.publishTimeAnchor = publishTimeAnchor;
	}

	public List<QuestionnaireDTO> getQuestionnaires() {
		return questionnaires;
	}

	public void setQuestionnaires(List<QuestionnaireDTO> questionnaires) {
		this.questionnaires = questionnaires;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
