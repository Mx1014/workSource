package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul> 提交表单信息
 * <li>namespaceId：域空间id</li>
 * <li>ownerType：ownerType</li>
 * <li>ownerId：ownerId</li>
 * <li>sourceId：sourceId</li>
 * <li>sourceType：sourceType</li>
 * <li>communityId : 项目id</li>
 * <li>currentOrganizationId：用户当前公司id</li>
 * <li>requisitionId : 表单Id</li>
 * <li>formOriginId: 表单id</li>
 * <li>formVersion: 表单版本</li>
 * <li>flowNodeId: 工作流节点ID</li>
 * <li>values: 审批项中，每项对应的值{@link PostApprovalFormItem} </li>
 * </ul>
 * @author janson
 *
 */
public class PostGeneralFormValCommand {
	private Integer namespaceId;
	private Long ownerId;
	private String ownerType;
	private Long approvalId;
	private String sourceType;
	private Long sourceId;
	private Long requisitionId;
	private Long communityId;
    private Long currentOrganizationId;
    
    private Long investmentAdId;
	private Long formOriginId;
	private Long formVersion;
	private Long flowNodeId;

    private Long orgId;

    private Long moduleId;

	@ItemType(PostApprovalFormItem.class)
	private List<PostApprovalFormItem> values;

	public Long getInvestmentAdId() {
		return investmentAdId;
	}

	public void setInvestmentAdId(Long investmentAdId) {
		this.investmentAdId = investmentAdId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public List<PostApprovalFormItem> getValues() {
		return values;
	}

	public void setValues(List<PostApprovalFormItem> values) {
		this.values = values;
	}

    public Long getCurrentOrganizationId() {
        return currentOrganizationId;
    }

    public void setCurrentOrganizationId(Long currentOrganizationId) {
        this.currentOrganizationId = currentOrganizationId;
    }

	public Long getRequisitionId() {
		return requisitionId;
	}

	public void setRequisitionId(Long requisitionId) {
		this.requisitionId = requisitionId;
	}

	public Long getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(Long approvalId) {
		this.approvalId = approvalId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getFormOriginId() {
		return formOriginId;
	}

	public void setFormOriginId(Long formOriginId) {
		this.formOriginId = formOriginId;
	}

	public Long getFormVersion() {
		return formVersion;
	}

	public void setFormVersion(Long formVersion) {
		this.formVersion = formVersion;
	}

	public Long getFlowNodeId() {
		return flowNodeId;
	}

	public void setFlowNodeId(Long flowNodeId) {
		this.flowNodeId = flowNodeId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
