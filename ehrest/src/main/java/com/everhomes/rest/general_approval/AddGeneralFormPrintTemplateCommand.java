package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerId: 所有者id，通用表单传入通用表单ID</li>
 * <li>ownerType: 所有者类型，通用配置为空</li>
 * <li>name: 模板名称</li>
 * <li>contents: 模板内容</li>
 * </ul>
 */
public class AddGeneralFormPrintTemplateCommand {

	private Long id;
	@NotNull
	private Integer namespaceId;
	private Long ownerId;
	private String ownerType;
	private String name;
	private String contents;
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
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
