// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>questionnaireId: 问卷id</li>
 * <li>targetType: 目标类型，organization</li>
 * <li>ownerId: 目标id，organization</li>
 * </ul>
 */
public class GetOrganizationQuestionnaireDetailCommand {

	private Integer namespaceId;

	private Long questionnaireId;

	private String targetType;

	private Long ownerId;

	public GetOrganizationQuestionnaireDetailCommand() {

	}

	public GetOrganizationQuestionnaireDetailCommand(Integer namespaceId, Long questionnaireId, String targetType, Long ownerId) {
		super();
		this.namespaceId = namespaceId;
		this.questionnaireId = questionnaireId;
		this.targetType = targetType;
		this.ownerId = ownerId;
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

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
