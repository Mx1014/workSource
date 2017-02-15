// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>questionnaireId: 问卷id</li>
 * <li>keywords: 查询关键字</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class GetQuestionnaireResultDetailCommand {

	private Integer namespaceId;

	private Long questionnaireId;

	private String keywords;

	private Long pageAnchor;

	private Integer pageSize;

	public GetQuestionnaireResultDetailCommand() {

	}

	public GetQuestionnaireResultDetailCommand(Integer namespaceId, Long questionnaireId, String keywords, Long pageAnchor, Integer pageSize) {
		super();
		this.namespaceId = namespaceId;
		this.questionnaireId = questionnaireId;
		this.keywords = keywords;
		this.pageAnchor = pageAnchor;
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

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
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
