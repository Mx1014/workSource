package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>ownerType: 拥有者类型：现在是comunity</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>name: 接收人姓名</li>
 *  <li>contactToken: 手机号或邮箱地址</li>
 *  <li>contactType: 推送类型</li>
 *  <li>categoryId: 服务联盟大类id</li>
 *  <li>status: 启用状态</li>
 * </ul>
 */
public class NotifyTargetDTO {
	
	private Long id;
	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;
	
	private String name;
	
	private String contactToken;
	
	private Byte contactType;
	
	private Long categoryId;
	
	private Byte status;

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

	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}

	public Byte getContactType() {
		return contactType;
	}

	public void setContactType(Byte contactType) {
		this.contactType = contactType;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
