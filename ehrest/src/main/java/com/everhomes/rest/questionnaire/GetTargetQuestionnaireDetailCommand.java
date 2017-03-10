// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>questionnaireId: 问卷id</li>
 * <li>targetType: 目标类型，organization</li>
 * <li>targetId: 目标id，organization</li>
 * </ul>
 */
public class GetTargetQuestionnaireDetailCommand {

	private Integer namespaceId;

	private Long questionnaireId;

	private String targetType;

	private Long targetId;

	public GetTargetQuestionnaireDetailCommand() {

	}

	public GetTargetQuestionnaireDetailCommand(Integer namespaceId, Long questionnaireId, String targetType, Long targetId) {
		super();
		this.namespaceId = namespaceId;
		this.questionnaireId = questionnaireId;
		this.targetType = targetType;
		this.targetId = targetId;
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

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
