package com.everhomes.rest.contract;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>contractId: 合同id</li>
 *     <li>contractNumber: 合同编号</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 * </ul>
 * Created by djm on 2018/8/9.
 */
public class EnterpriseContractCommand {
	@NotNull
    private Long contractId;
	@NotNull
    private Integer namespaceId;
	@NotNull
    private Long communityId;

    private Long organizationId;
    @NotNull
    private Long categoryId;
    
    public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public EnterpriseContractCommand() {
    }

    public EnterpriseContractCommand(Long contractId, Integer namespaceId, Long communityId) {
        this.contractId = contractId;
        this.namespaceId = namespaceId;
        this.communityId = communityId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
