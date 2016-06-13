package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * communityFlagId: 标示是哪个园区app的id
 *
 */
public class ListActivityCategoriesCommand {
	
	private Long parentId;
	
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

    
    public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
