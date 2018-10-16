package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contactToken: 电话号码</li>
 * <li>userId: 用户id</li>
 * <li>namespaceId: 域空间ID</li>
 * </ul>
 */
public class QryCommunityUserAllByUserIdCommand {
	
	private String contactToken;

	private Long userId;

	private Integer namespaceId;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
