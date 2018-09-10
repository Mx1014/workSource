package com.everhomes.rest.contract;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>categoryId: 合同类型多入口</li>
 * <li>ownerId: 所有者id</li>
 * <li>contractId: 合同id</li>
 * <li>contractNumber: 合同编号</li>
 * <li>templateId: 模板id</li>
 * </ul>
 * Created by jm.ding on 2018/6/27.
 */
public class SetPrintContractTemplateCommand {

	private Long id;
	@NotNull
	private Integer namespaceId;
	@NotNull
	private Long categoryId;
	private Long ownerId;
	@NotNull
	private Long contractId;
	private String contractNumber;
	@NotNull
	private Long orgId;
	
	private Long templateId;
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
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Long getContractId() {
		return contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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
