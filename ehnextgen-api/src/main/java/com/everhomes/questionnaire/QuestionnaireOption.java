// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.server.schema.tables.pojos.EhQuestionnaireOptions;
import com.everhomes.util.StringHelper;

public class QuestionnaireOption extends EhQuestionnaireOptions {

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}