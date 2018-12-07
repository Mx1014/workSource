package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>contractId: 合同id</li>
 * <li>categoryId: 合同类型多入口</li>
 * <li>templateId: 合同模板id</li>
 * <li>templateName: 合同模板名称</li>
 * <li>ownerId: 所有者id(园区id)</li>
 * <li>ownerType: 所有者类型，通用配置为空，项目下配置为"community"</li>
 * <li>orgId: 公司id</li>
 * <li>communityId: 园区id</li>
 * </ul>
 */
public class GenerateContractDocumentsCommand {
	
	private Integer namespaceId;
	private Long contractId;
	private Long categoryId;
	private Long templateId;
	private String templateName;
	private Long ownerId = 0L;
	private String ownerType;
	private Long orgId;
	private Long communityId;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	
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

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
