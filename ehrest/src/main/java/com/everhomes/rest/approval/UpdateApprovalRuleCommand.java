// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: id</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.approval.ApprovalOwnerType}
 * </li>
 * <li>ownerId: 所属者ID</li>
 * <li>name: 审批规则名称</li>
 * <li>ruleFlowMapList: 审批规则与审批流程关联列表，参考
 * {@link com.everhomes.rest.approval.RuleFlowMap}</li>
 * </ul>
 */
public class UpdateApprovalRuleCommand {
	private Long id;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private String name;
	@ItemType(RuleFlowMap.class)
	private List<RuleFlowMap> ruleFlowMapList;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
