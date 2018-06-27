package com.everhomes.rest.contract;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * 参数:
 * <li>namespaceId: 域空间id</li>
 * <li>communityId: 园区id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/17.
 */
public class AddContractTemplateCommand {

	private Long id;
	@NotNull
	private Integer namespaceId;
	@NotNull
	private Long categoryId;
	private Long ownerId;
	private String ownerType;
	private String name;
	private Byte contracttemplateType;
	private Byte status;
	private String contents;
	private Long parentId;
	private Long version;
	@NotNull
	private Long orgId;
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
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Byte getContracttemplateType() {
		return contracttemplateType;
	}
	public void setContracttemplateType(Byte contracttemplateType) {
		this.contracttemplateType = contracttemplateType;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	
}
