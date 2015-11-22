package com.everhomes.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * communityFlagId: 标示是哪个园区app的id
 *
 */
public class ListActivityCategoriesCommand {
	
	private Long communityFlagId;
	
	private Integer namespaceId;

	public Long getCommunityFlagId() {
		return communityFlagId;
	}

	public void setCommunityFlagId(Long communityFlagId) {
		this.communityFlagId = communityFlagId;
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
