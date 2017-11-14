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
 * <li>organizationId: 当前场景公司的id</li>
 * <li>collectFlag: 问卷状态（进行中，已结束），参考{@link com.everhomes.rest.questionnaire.QuestionnaireCollectFlagType}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListTargetQuestionnairesCommand {
//	 * <li>answerTimeAnchor: 回答时间的锚点</li>
// * <li>publishTimeAnchor: 发布时间的锚点</li>

	private Integer namespaceId;

	private String ownerType;

	private Long ownerId;

	private String targetType;
//
//	private Long targetId;

	private Long organizationId;

	private Byte collectFlag;

	private Timestamp nowTime = new Timestamp(System.currentTimeMillis());//用于和截止日期比较,定一个状态而已。

//	private Long answerTimeAnchor;

//	private Long publishTimeAnchor;
	private String pageAnchor;

	private Integer pageSize;

	public ListTargetQuestionnairesCommand() {

	}

	public ListTargetQuestionnairesCommand(Integer namespaceId, String ownerType, Long ownerId, String targetType, Long organizationId, Byte collectFlag, Timestamp nowTime, String pageAnchor, Integer pageSize) {
		this.namespaceId = namespaceId;
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.targetType = targetType;
		this.organizationId = organizationId;
		this.collectFlag = collectFlag;
		this.nowTime = nowTime;
		this.pageAnchor = pageAnchor;
		this.pageSize = pageSize;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(String pageAnchor) {
		this.pageAnchor = pageAnchor;
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
