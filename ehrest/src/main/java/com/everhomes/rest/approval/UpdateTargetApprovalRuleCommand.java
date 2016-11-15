package com.everhomes.rest.approval;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * <ul> 
 * <li>ownerType：所属对象类型organization </li>
 * <li>ownerId：所属对象id</li>
 * <li>targetType：映射目标类型(规则是设置给谁的) organization/user</li>
 * <li>targetId：映射目标 id</li> 
 * <li>ruleFlowMapList: 审批规则与审批流程关联列表，参考{@link com.everhomes.rest.approval.RuleFlowMap}</li>
 * </ul>
 */
public class UpdateTargetApprovalRuleCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	
	private String targetType;
	private Long targetId;
	
	@ItemType(RuleFlowMap.class)
	private List<RuleFlowMap> ruleFlowMapList;

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

	public List<RuleFlowMap> getRuleFlowMapList() {
		return ruleFlowMapList;
	}

	public void setRuleFlowMapList(List<RuleFlowMap> ruleFlowMapList) {
		this.ruleFlowMapList = ruleFlowMapList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
