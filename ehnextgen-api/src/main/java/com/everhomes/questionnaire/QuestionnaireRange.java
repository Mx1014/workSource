// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.server.schema.tables.pojos.EhQuestionnaireRanges;
import com.everhomes.util.StringHelper;

import java.util.List;

public class QuestionnaireRange extends EhQuestionnaireRanges {
	
	private static final long serialVersionUID = -1175368502943062908L;

	private List<String> adminlists;

	public List<String> getAdminlists() {
		return adminlists;
	}

	public void setAdminlists(List<String> adminlists) {
		this.adminlists = adminlists;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}