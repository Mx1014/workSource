// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.server.schema.tables.pojos.EhQuestionnaireQuestions;
import com.everhomes.util.StringHelper;

public class QuestionnaireQuestion extends EhQuestionnaireQuestions {

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}