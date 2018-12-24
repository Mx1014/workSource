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
 * <li>contracttemplateType: 0 收款合同模板 1付款合同模板</li>
 * <li>status: 0: inactive, 1: confirming, 2: active</li>
 * <li>contents: 模板内容</li>
 * <li>parentId: 复制于哪个合同模板</li>
 * <li>version: 版本记录</li>
 * <li>creatorUid: 创建者uid</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 * Created by jm.ding on 2018/6/27.
 */
public class listContractTemplateCommand {

	private Long id;
	@NotNull
	private Integer namespaceId;
	@NotNull
	private Long categoryId;
	private Long ownerId;
	private String ownerType;
	private String name;
	@NotNull
	private Long orgId;
	private Long pageAnchor;
	private Integer pageSize;
	private Long appId;
	
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
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
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
