// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.server.schema.tables.pojos.EhQuestionnaireAnswers;
import com.everhomes.util.StringHelper;

public class QuestionnaireAnswer extends EhQuestionnaireAnswers {

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}