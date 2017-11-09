// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.server.schema.tables.pojos.EhQuestionnaireAnswers;
import com.everhomes.util.StringHelper;

public class QuestionnaireAnswer extends EhQuestionnaireAnswers {

	private Byte questionType;
	private String optionName;
	private String optionUri;

	private static final long serialVersionUID = 2664030858494258440L;

	public Byte getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Byte questionType) {
		this.questionType = questionType;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionUri() {
		return optionUri;
	}

	public void setOptionUri(String optionUri) {
		this.optionUri = optionUri;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}