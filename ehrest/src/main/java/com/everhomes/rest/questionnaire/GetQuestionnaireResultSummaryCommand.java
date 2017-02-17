// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>questionnaireId: 问卷id</li>
 * <li>pageSize: 填空题每页显示多少条</li>
 * </ul>
 */
public class GetQuestionnaireResultSummaryCommand {

	private Integer namespaceId;

	private Long questionnaireId;
	
	private Integer pageSize;

	public GetQuestionnaireResultSummaryCommand() {

	}

	public GetQuestionnaireResultSummaryCommand(Integer namespaceId, Long questionnaireId) {
		super();
		this.namespaceId = namespaceId;
		this.questionnaireId = questionnaireId;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
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
