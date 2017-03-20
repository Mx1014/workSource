// @formatter:off
package com.everhomes.rest.questionnaire;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>questionId: 问题id</li>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>options: 填空题答案列表，参考{@link com.everhomes.rest.questionnaire.QuestionnaireOptionDTO}</li>
 * </ul>
 */
public class ListBlankQuestionAnswersResponse {

	private Long questionId;
	
	private Long nextPageAnchor;

	@ItemType(QuestionnaireOptionDTO.class)
	private List<QuestionnaireOptionDTO> options;

	public ListBlankQuestionAnswersResponse() {

	}

	public ListBlankQuestionAnswersResponse(Long questionId, Long nextPageAnchor, List<QuestionnaireOptionDTO> options) {
		super();
		this.questionId = questionId;
		this.nextPageAnchor = nextPageAnchor;
		this.options = options;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<QuestionnaireOptionDTO> getOptions() {
		return options;
	}

	public void setOptions(List<QuestionnaireOptionDTO> options) {
		this.options = options;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
