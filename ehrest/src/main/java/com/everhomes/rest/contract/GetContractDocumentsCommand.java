package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>orgId: 公司id</li>
 * <li>communityId: 园区id</li>
 * <li>id: 合同文档id</li>
 * <li>contractId: 合同id</li>
 * </ul>
 */
public class GetContractDocumentsCommand {
	
	private Integer namespaceId;
	private Long orgId;
	private Long communityId;
	private Long id;
	private Long contractId;
	
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

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
