package com.everhomes.rest.techpark.punch.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 
 * <li>ownerType：所属对象类型organization</li>
 * <li>ownerId：所属对象id</li>
 * <li>targetType：映射目标类型(规则是设置给谁的) organization/user</li>
 * <li>targetId：映射目标 id</li> 
 * </ul>
 */
public class GetTargetPunchAllRuleCommand {

	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	
	private String targetType;
	private Long targetId;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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
}
