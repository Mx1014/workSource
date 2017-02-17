// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.server.schema.tables.pojos.EhQuestionnaireAnswers;
import com.everhomes.util.StringHelper;

public class QuestionnaireAnswer extends EhQuestionnaireAnswers {

	private static final long serialVersionUID = 2664030858494258440L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}