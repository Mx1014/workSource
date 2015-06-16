// @formatter:off
package com.everhomes.pm;


import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛Id</li>
 * <li>communityId: 小区Id</li>
 * <li>topicId: 帖子ID</li>
 * <li>userId: 接收任务的用户Id</li>
 * </ul>
 */
public class AssginPmTopicCommand {
	private Long communityId;
	private Long topicId;
    private Long userId;
   
    
    public AssginPmTopicCommand() {
    }

    public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
