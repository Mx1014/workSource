// @formatter:off
package com.everhomes.rest.questionnaire;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>options: 填空题答案列表</li>
 * </ul>
 */
public class ListBlankQuestionAnswersResponse {

	private Long nextPageAnchor;

	@ItemType(QuestionnaireOptionDTO.class)
	private List<QuestionnaireOptionDTO> options;

	public ListBlankQuestionAnswersResponse() {

	}

	public ListBlankQuestionAnswersResponse(Long nextPageAnchor, List<QuestionnaireOptionDTO> options) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.options = options;
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
