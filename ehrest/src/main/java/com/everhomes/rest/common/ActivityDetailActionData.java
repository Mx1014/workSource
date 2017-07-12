// @formatter:off
package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

/**
 * 活动详情的actionData
 * <ul>
 * <li>forumId: 论坛id</li>
 * <li>topicId: 帖子id</li>
 * </ul>
 */
public class ActivityDetailActionData {
	private Long forumId;
	private Long topicId;

	public ActivityDetailActionData() {
		super();
	}

	public ActivityDetailActionData(Long forumId, Long topicId) {
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
