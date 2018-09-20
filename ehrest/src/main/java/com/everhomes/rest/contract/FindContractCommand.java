package com.everhomes.rest.contract;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 合同id</li>
 *     <li>contractNumber: 合同编号</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 *     <li>partyAId: 合同甲方id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public class FindContractCommand {
	@NotNull
    private Long id;

    private String contractNumber;
    @NotNull
    private Integer namespaceId;
    @NotNull
    private Long communityId;

    private Long partyAId;

    private Long organizationId;
    @NotNull
    private Long categoryId;

    private Long orgId;

    
    public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public FindContractCommand() {
    }

    public FindContractCommand(Long id, String contractNumber, Integer namespaceId, Long communityId, Long partyAId) {
        this.id = id;
        this.contractNumber = contractNumber;
        this.namespaceId = namespaceId;
        this.communityId = communityId;
        this.partyAId = partyAId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
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

    public Long getPartyAId() {
        return partyAId;
    }

    public void setPartyAId(Long partyAId) {
        this.partyAId = partyAId;
    }


    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
