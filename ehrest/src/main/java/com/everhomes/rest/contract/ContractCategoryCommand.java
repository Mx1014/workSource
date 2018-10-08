package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>contractNumber: 合同编号</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 * </ul>
 * Created by djm on 2018/8/9.
 */
public class ContractCategoryCommand {
    private Integer namespaceId;
    private Long communityId;
    private Long organizationId;
    private Long categoryId;
    
    public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public ContractCategoryCommand() {
    }

    public ContractCategoryCommand(Long contractId, Integer namespaceId, Long communityId) {
        this.namespaceId = namespaceId;
        this.communityId = communityId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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
