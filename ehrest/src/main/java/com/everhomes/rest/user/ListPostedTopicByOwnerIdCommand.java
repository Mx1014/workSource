package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>帖子或评论信息：</p>
 * <ul>
 * <li>ownerUid: 用户ID</li>
 * <li>communityId: 用户所在小区ID</li>
 * <li>excludeCategories: 排除类型 {@link com.everhomes.rest.category.CategoryConstants}</li>
 * </ul>
 */
public class ListPostedTopicByOwnerIdCommand {
    private Long ownerUid;
    
    private Long communityId;
    
    @ItemType(Long.class)
    private List<Long> excludeCategories;

    public Long getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(Long ownerUid) {
        this.ownerUid = ownerUid;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<Long> getExcludeCategories() {
		return excludeCategories;
	}

	public void setExcludeCategories(List<Long> excludeCategories) {
		this.excludeCategories = excludeCategories;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
