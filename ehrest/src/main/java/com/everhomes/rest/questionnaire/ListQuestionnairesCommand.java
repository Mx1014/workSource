// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 所属类型，community</li>
 * <li>ownerId: 所属id，communityId</li>
 * <li>startTime: 起始时间搓</li>
 * <li>endTime: 结束时间搓</li>
 * <li>targetType: 收集对象，参考 {@link com.everhomes.rest.questionnaire.QuestionnaireTargetType}</li>
 * <li>status: 问卷状态（已发布，草稿），参考 {@link com.everhomes.rest.questionnaire.QuestionnaireStatus}</li>
 * <li>collectFlag: 问卷收集状态（收集中，收集完成），参考 {@link com.everhomes.rest.questionnaire.QuestionnaireCollectFlagType}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListQuestionnairesCommand {

	private Integer namespaceId;

	private String ownerType;

	private Long ownerId;

	private Long startTime;

	private Timestamp nowTime = new Timestamp(System.currentTimeMillis());//用于和截止日期比较,定一个状态而已。

	private Long endTime;

	private String targetType;

	private Byte status;

	private Byte collectFlag;

	private Long pageAnchor;

	private Integer pageSize;

	public ListQuestionnairesCommand() {

	}

	public ListQuestionnairesCommand(Integer namespaceId, String ownerType, Long ownerId, Long startTime, Long endTime, String targetType, Byte status, Byte collectFlag, Long pageAnchor, Integer pageSize) {
		this.namespaceId = namespaceId;
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.targetType = targetType;
		this.status = status;
		this.collectFlag = collectFlag;
		this.pageAnchor = pageAnchor;
		this.pageSize = pageSize;
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

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getCollectFlag() {
		return collectFlag;
	}

	public void setCollectFlag(Byte collectFlag) {
		this.collectFlag = collectFlag;
	}

	public Timestamp getNowTime() {
		return nowTime;
	}

	public void setNowTime(Timestamp nowTime) {
		this.nowTime = nowTime;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
