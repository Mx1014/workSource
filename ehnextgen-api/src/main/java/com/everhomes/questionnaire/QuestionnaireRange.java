// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.server.schema.tables.pojos.EhQuestionnaireRanges;
import com.everhomes.util.StringHelper;

public class QuestionnaireRange extends EhQuestionnaireRanges {
	
	private static final long serialVersionUID = -1175368502943062908L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}