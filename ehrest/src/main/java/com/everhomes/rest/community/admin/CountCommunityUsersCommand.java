package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * namespaceId: 命名空间
 *
 */
public class CountCommunityUsersCommand {
	
	private Integer namespaceId;

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
