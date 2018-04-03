// @formatter:off
package com.everhomes.rest.questionnaire;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>questionnaires: 问卷调查列表，参考{@link com.everhomes.rest.questionnaire.QuestionnaireDTO}</li>
 * </ul>
 */
public class ListOrganizationQuestionnairesResponse {

	private Long nextPageAnchor;

	@ItemType(QuestionnaireDTO.class)
	private List<QuestionnaireDTO> questionnaires;

	public ListOrganizationQuestionnairesResponse() {

	}

	public ListOrganizationQuestionnairesResponse(Long nextPageAnchor, List<QuestionnaireDTO> questionnaires) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.questionnaires = questionnaires;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
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
