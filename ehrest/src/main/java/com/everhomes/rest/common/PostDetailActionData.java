package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为TOPIC_DETAILS时跳转到帖子详情
 * <li>forumId: 论坛ID
 * <li>topicId: 帖子ID</li>
 * </ul>
 */
public class PostDetailActionData implements Serializable{
    private static final long serialVersionUID = 7502654058025166257L;
    //{"forumId": 1,"topicId":1}  
    private Long forumId;
    private Long topicId;
    
    public PostDetailActionData() {
		super();
	}

	public PostDetailActionData(Long forumId, Long topicId) {
		super();
		this.forumId = forumId;
		this.topicId = topicId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
	public String toUrlString(String url) {
		return url.replace("${forumId}", String.valueOf(forumId)).replace("${topicId}", String.valueOf(topicId));
	}
}
