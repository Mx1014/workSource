// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>questionId: 题目id</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListBlankQuestionAnswersCommand {

	private Integer namespaceId;

	private Long questionId;

	private Long pageAnchor;

	private Integer pageSize;

	public ListBlankQuestionAnswersCommand() {

	}

	public ListBlankQuestionAnswersCommand(Integer namespaceId, Long questionId, Long pageAnchor, Integer pageSize) {
		super();
		this.namespaceId = namespaceId;
		this.questionId = questionId;
		this.pageAnchor = pageAnchor;
		this.pageSize = pageSize;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
