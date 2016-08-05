package com.everhomes.rest.yellowPage;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: id</li>
 *  <li>ownerType: 拥有者类型：现在是comunity</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>name: 名称</li>
 *  <li>displayName: 简称</li>
 *  <li>contact: 咨询电话</li>
 *  <li>description: 介绍</li>
 *  <li>posterUri: 标题图</li>
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
