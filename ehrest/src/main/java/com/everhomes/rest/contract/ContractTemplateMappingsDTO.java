package com.everhomes.rest.contract;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>categoryId: 合同类型多入口</li>
 * <li>ownerId: 所有者id</li>
 * <li>ownerType: 所有者类型，通用配置为空</li>
 * <li>contract_id: 合同id</li>
 * <li>template_id: 模板id</li>
 * <li>parentId: 复制于哪个合同模板</li>
 * <li>version: 版本记录</li>
 * <li>creatorUid: 创建者uid</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 * Created by jm.ding on 2018/6/27.
 */
public class ContractTemplateMappingsDTO {
	private Long id;
	private Integer namespaceId;
	private Long categoryId;
	private Long ownerId;
	private String ownerType;
	private Long contract_id;
	private Long template_id;
	private Byte status;
	private Long parentId;
	private Integer version;
	private Timestamp createTime;
	private Long creatorUid;
	
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

	public Long getContract_id() {
		return contract_id;
	}

	public void setContract_id(Long contract_id) {
		this.contract_id = contract_id;
	}

	public Long getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(Long template_id) {
		this.template_id = template_id;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
