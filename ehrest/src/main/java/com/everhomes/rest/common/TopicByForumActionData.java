package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为TOPIC_BY_FORUM(31)，按指定论坛查帖列表，调用接口/forum/listTopics
 * <li>forumId：论坛ID</li>
 * </ul>
 */
public class TopicByForumActionData implements Serializable{
    private static final long serialVersionUID = -742724365939053762L;
    
    private Long forumId;
    
    private Byte visibilityScope;
    
    private Long communityId;
    

    public Byte getVisibilityScope() {
		return visibilityScope;
	}

	public void setVisibilityScope(Byte visibilityScope) {
		this.visibilityScope = visibilityScope;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
