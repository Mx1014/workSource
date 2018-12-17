package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>orgId: 公司id</li>
 * <li>communityId: 园区id</li>
 * <li>contractId: 合同id</li>
 * <li>id: 合同文档id</li>
 * <li>content: 合同文档内容</li>
 * </ul>
 */
public class UpdateContractDocumentsCommand {
	
	private Integer namespaceId;
	private Long orgId;
	private Long communityId;
	private Long contractId;
	private Long id;
	private String content;
	private Long templateId;
	
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
	public Long getContractId() {
		return contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
