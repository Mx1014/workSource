// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.server.schema.tables.pojos.EhQuestionnaires;
import com.everhomes.util.StringHelper;

import java.util.List;

public class Questionnaire extends EhQuestionnaires {

	private static final long serialVersionUID = 7910613439841184323L;
	private List<QuestionnaireRange> ranges;

	public List<QuestionnaireRange> getRanges() {
		return ranges;
	}

	public void setRanges(List<QuestionnaireRange> ranges) {
		this.ranges = ranges;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}