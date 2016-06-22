package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>postId: 帖子id</li>
 *  <li>forumId: 论坛id</li>
 * </ul>
 *
 */
public class ActivityTokenDTO {
	
	private Long postId;
	
	private Long forumId;

	
	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
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
