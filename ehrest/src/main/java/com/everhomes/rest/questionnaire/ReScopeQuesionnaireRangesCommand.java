// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

import java.util.List;

public class ReScopeQuesionnaireRangesCommand {

	private Long quesionnaireId;

	public Long getQuesionnaireId() {
		return quesionnaireId;
	}

	public void setQuesionnaireId(Long quesionnaireId) {
		this.quesionnaireId = quesionnaireId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
