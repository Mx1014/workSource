package com.everhomes.rest.contract;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>categoryId: 合同类型多入口</li>
 * <li>ownerId: 所有者id</li>
 * <li>ownerType: 所有者类型，通用配置为空</li>
 * <li>name: 模板名称</li>
 * <li>contents: 模板内容</li>
 * <li>version: 版本记录</li>
 * <li>initParams: 合同模板初始化参数（计价条款、关联资产等的数目），前端会解析这个json</li>
 * </ul>
 * Created by jm.ding on 2018/6/27.
 */
public class UpdateContractTemplateCommand {

	@NotNull
	private Long id;
	@NotNull
	private Integer namespaceId;
	@NotNull
	private Long categoryId;
	private Long ownerId;
	private String ownerType;
	private String name;
	private String contents;
	@NotNull
	private Long orgId;
	private String initParams;
	
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
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getInitParams() {
		return initParams;
	}
	public void setInitParams(String initParams) {
		this.initParams = initParams;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
