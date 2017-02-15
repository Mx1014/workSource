// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.server.schema.tables.pojos.EhQuestionnaires;
import com.everhomes.util.StringHelper;

public class Questionnaire extends EhQuestionnaires {

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}