// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>questionnaireId: 问卷id</li>
 * </ul>
 */
public class GetQuestionnaireDetailCommand {

	private Integer namespaceId;

	private Long questionnaireId;

	public GetQuestionnaireDetailCommand() {

	}

	public GetQuestionnaireDetailCommand(Integer namespaceId, Long questionnaireId) {
		super();
		this.namespaceId = namespaceId;
		this.questionnaireId = questionnaireId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Long questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
