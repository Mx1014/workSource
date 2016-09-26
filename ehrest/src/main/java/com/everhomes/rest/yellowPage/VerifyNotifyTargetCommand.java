package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>ownerType: 拥有者类型：现在是comunity</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>contactToken: 手机号或邮箱地址</li>
 *  <li>categoryId: 服务联盟大类id</li>
 * </ul>
 */
public class VerifyNotifyTargetCommand {
	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;
	
	private String contactToken;
	
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

	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

}
