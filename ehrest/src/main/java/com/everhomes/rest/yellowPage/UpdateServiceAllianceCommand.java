package com.everhomes.rest.yellowPage;


import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: id</li>
 *  <li>namespaceId: 域空间id</li>
 *  <li>ownerType: 拥有者类型：现在是comunity</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>name: 名称</li>
 *  <li>displayName: 简称</li>
 *  <li>contact: 咨询电话</li>
 *  <li>description: 介绍</li>
 *  <li>posterUri: 标题图</li>
 *  <li>type:类型  </li>
 *  <li>skipType: 只有一个企业时是否跳过列表页，0 不跳； 1 跳过</li>
 *  <li>displayMode:类型 {@link com.everhomes.rest.yellowPage.ServiceAllianceCategoryDisplayMode} </li>
 *  <li>coverAttachments: 封面图片文件 服务联盟v3.4 {@link com.everhomes.rest.yellowPage.ServiceAllianceAttachmentDTO} </li>
 * </ul>
 */
public class UpdateServiceAllianceCommand {

	private Long     id;
	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;
	
	private String   name;
	
	private String   displayName;
	
	private String   contact;
	
	private String   description;
	
	private String   posterUri;
	
	private Long type;

	private Byte displayMode;
	
	private Byte skipType;
	
	private Integer namespaceId;
	
	@ItemType(ServiceAllianceAttachmentDTO.class)
	private List<ServiceAllianceAttachmentDTO> coverAttachments;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(Byte displayMode) {
		this.displayMode = displayMode;
	}

	public Byte getSkipType() {
		return skipType;
	}

	public void setSkipType(Byte skipType) {
		this.skipType = skipType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPosterUri() {
		return posterUri;
	}

	public void setPosterUri(String posterUri) {
		this.posterUri = posterUri;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<ServiceAllianceAttachmentDTO> getCoverAttachments() {
		return coverAttachments;
	}

	public void setCoverAttachments(List<ServiceAllianceAttachmentDTO> coverAttachments) {
		this.coverAttachments = coverAttachments;
	}
}
