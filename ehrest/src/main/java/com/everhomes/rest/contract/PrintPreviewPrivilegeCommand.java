package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>orgId: orgId</li>
 *     <li>communityId: 园区Id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>categoryId: 多入口</li>
 * </ul>
 * Created by dingjianmin on 2018/7/05.
 */
public class PrintPreviewPrivilegeCommand {
    private Long orgId;
    private Long communityId;
    private Integer namespaceId;
	private Long categoryId;
	private Long contractId;

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}


    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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
    
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
