package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 
 * <li>id：id</li>
 * <li>ownerType：所属对象类型organization/user</li>
 * <li>ownerId：所属对象id</li>
 * <li>ownerType：映射目标类型(规则是设置给谁的) organization/user</li>
 * <li>targetId：映射目标 id</li>
 * <li>punchRuleId: 打卡规则id</li>
 * <li>reviewRuleId: 审批规则id</li>
 * <li>description: 描述</li>
 * </ul>
 */
public class PunchRuleMapDTO {
	private Long id;
	private String ownerType;
	private Long ownerId;
	private String targetType;
	private Long targetId;
	private Long punchRuleId;
	private Long reviewRuleId;
	private String description;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getPunchRuleId() {
		return punchRuleId;
	}

	public void setPunchRuleId(Long punchRuleId) {
		this.punchRuleId = punchRuleId;
	}

	public Long getReviewRuleId() {
		return reviewRuleId;
	}

	public void setReviewRuleId(Long reviewRuleId) {
		this.reviewRuleId = reviewRuleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
