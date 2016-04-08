// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>topicId: 帖子ID</li>
 * <li>communityId: 用户当前所在的小区ID，如果没有则不填</li>
 * <li>option :  process（处理） 和 grab（抢单） none（无操作）</li>
 * <li>entrancePrivilege标识 :  权限标识</li>
 * </ul>
 */
public class GetTopicCommand {
    private Long forumId;
    private Long topicId;
    private Long communityId;
    private String option;
	
	private String entrancePrivilege;
    
    public GetTopicCommand() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }
    
    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    
    public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getEntrancePrivilege() {
		return entrancePrivilege;
	}

	public void setEntrancePrivilege(String entrancePrivilege) {
		this.entrancePrivilege = entrancePrivilege;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
