// @formatter:off
package com.everhomes.rest.questionnaire;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>问卷调查
 * <li>id: id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 所属类型，community</li>
 * <li>ownerId: 所属id，communityId</li>
 * <li>questionnaireName: 名称</li>
 * <li>description: 描述</li>
 * <li>collectionCount: 收集量</li>
 * <li>status: 状态，1草稿，2已发布</li>
 * <li>publishTime: 发布时间</li>
 * <li>createTime: 创建时间</li>
 * <li>submitTime: 企业问卷提交时间</li>
 * <li>questions: 题目列表，参考{@link com.everhomes.rest.questionnaire.QuestionnaireQuestionDTO}</li>
 * </ul>
 */
public class QuestionnaireDTO {
	private Long id;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private String questionnaireName;
	private String description;
	private Integer collectionCount;
	private Byte status;
	private Long publishTime;
	private Long createTime;
	private Long submitTime;
	@ItemType(QuestionnaireQuestionDTO.class)
	private List<QuestionnaireQuestionDTO> questions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getQuestionnaireName() {
		return questionnaireName;
	}

	public void setQuestionnaireName(String questionnaireName) {
		this.questionnaireName = questionnaireName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(Integer collectionCount) {
		this.collectionCount = collectionCount;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public List<QuestionnaireQuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionnaireQuestionDTO> questions) {
		this.questions = questions;
	}

	public Long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Long publishTime) {
		this.publishTime = publishTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Long submitTime) {
		this.submitTime = submitTime;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
