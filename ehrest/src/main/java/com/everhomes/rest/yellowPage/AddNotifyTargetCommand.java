package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>ownerType: 拥有者类型：现在是comunity</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>name: 接收人姓名</li>
 *  <li>contactToken: 手机号或邮箱地址</li>
 *  <li>contactType: 推送类型</li>
 *  <li>categoryId: 服务联盟大类id</li>
 * </ul>
 */
public class AddNotifyTargetCommand {
	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;
	
	private String name;
	
	private String contactToken;
	
	private Byte contactType;
	
	private Long categoryId;

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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
