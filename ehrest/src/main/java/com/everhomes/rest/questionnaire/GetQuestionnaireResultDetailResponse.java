// @formatter:off
package com.everhomes.rest.questionnaire;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>questionnaireResultTargets: 问卷调查结果目标列表，参考{@link com.everhomes.rest.questionnaire.QuestionnaireResultTargetDTO}</li>
 * </ul>
 */
public class GetQuestionnaireResultDetailResponse {

	private Long nextPageAnchor;

	@ItemType(QuestionnaireResultTargetDTO.class)
	private List<QuestionnaireResultTargetDTO> questionnaireResultTargets;

	public GetQuestionnaireResultDetailResponse() {

	}

	public GetQuestionnaireResultDetailResponse(Long nextPageAnchor, List<QuestionnaireResultTargetDTO> questionnaireResultTargets) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.questionnaireResultTargets = questionnaireResultTargets;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<QuestionnaireResultTargetDTO> getQuestionnaireResultTargets() {
		return questionnaireResultTargets;
	}

	public void setQuestionnaireResultTargets(List<QuestionnaireResultTargetDTO> questionnaireResultTargets) {
		this.questionnaireResultTargets = questionnaireResultTargets;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
