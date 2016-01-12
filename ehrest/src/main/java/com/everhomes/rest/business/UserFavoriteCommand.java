package com.everhomes.rest.business;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 用户ID</li>
 * <li>id: 店铺ID</li>
 * <li>namespaceId: 命名空间</li>
 * </ul>
 */

public class UserFavoriteCommand{
    @NotNull
    private Long userId;
    @NotNull
    private String id;
    private Integer namespaceId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
