// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 所属类型，community</li>
 * <li>ownerId: 所属id，communityId</li>
 * <li>targetType: 目标类型，参考 {@link com.everhomes.rest.questionnaire.QuestionnaireTargetType}</li>
 * <li>targetId: 目标id</li>
 * <li>collectFlag: 问卷状态（进行中，已结束），参考{@link com.everhomes.rest.questionnaire.QuestionnaireCollectFlagType}</li>
 * <li>answerTimeAnchor: 回答时间的锚点</li>
 * <li>publishTimeAnchor: 发布时间的锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListTargetQuestionnairesCommand {

	private Integer namespaceId;

	private String ownerType;

	private Long ownerId;

	private String targetType;

	private Long targetId;

	private Byte collectFlag;

	private Timestamp nowTime = new Timestamp(System.currentTimeMillis());//用于和截止日期比较,定一个状态而已。

	private Long answerTimeAnchor;

	private Long publishTimeAnchor;

	private Integer pageSize;

	public ListTargetQuestionnairesCommand() {

	}

	public ListTargetQuestionnairesCommand(Integer namespaceId, String ownerType, Long ownerId, String targetType, Long targetId, Byte collectFlag, Timestamp nowTime, Long answerTimeAnchor, Long publishTimeAnchor, Integer pageSize) {
		this.namespaceId = namespaceId;
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.targetType = targetType;
		this.targetId = targetId;
		this.collectFlag = collectFlag;
		this.nowTime = nowTime;
		this.answerTimeAnchor = answerTimeAnchor;
		this.publishTimeAnchor = publishTimeAnchor;
		this.pageSize = pageSize;
	}

	public Long getAnswerTimeAnchor() {
		return answerTimeAnchor;
	}

	public void setAnswerTimeAnchor(Long answerTimeAnchor) {
		this.answerTimeAnchor = answerTimeAnchor;
	}

	public Long getPublishTimeAnchor() {
		return publishTimeAnchor;
	}

	public void setPublishTimeAnchor(Long publishTimeAnchor) {
		this.publishTimeAnchor = publishTimeAnchor;
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

	public Timestamp getNowTime() {
		return nowTime;
	}

	public void setNowTime(Timestamp nowTime) {
		this.nowTime = nowTime;
	}

	public Byte getCollectFlag() {
		return collectFlag;
	}

	public void setCollectFlag(Byte collectFlag) {
		this.collectFlag = collectFlag;
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
