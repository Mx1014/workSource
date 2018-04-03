// @formatter:off
package com.everhomes.rest.questionnaire;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>targets: 目标列表，参考{@link com.everhomes.rest.questionnaire.QuestionnaireResultTargetDTO}</li>
 * </ul>
 */
public class ListOptionTargetsResponse {

	private Long nextPageAnchor;

	@ItemType(QuestionnaireResultTargetDTO.class)
	private List<QuestionnaireResultTargetDTO> targets;

	public ListOptionTargetsResponse() {

	}

	public ListOptionTargetsResponse(Long nextPageAnchor, List<QuestionnaireResultTargetDTO> targets) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.targets = targets;
	}

	public List<QuestionnaireResultTargetDTO> getTargets() {
		return targets;
	}

	public void setTargets(List<QuestionnaireResultTargetDTO> targets) {
		this.targets = targets;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
