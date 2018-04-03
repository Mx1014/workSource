// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.server.schema.tables.pojos.EhQuestionnaireQuestions;
import com.everhomes.util.StringHelper;

public class QuestionnaireQuestion extends EhQuestionnaireQuestions {

	private static final long serialVersionUID = -8404481321089101318L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}