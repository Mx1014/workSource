package com.everhomes.rest.forum;

import com.everhomes.rest.activity.VideoSupportType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>forumEntryId: 入口Id</li>
 *     <li>tag: tag</li>
 * </ul>
 */
public class ForumActionData {

	private Long forumEntryId;

	private String tag;

	public Long getForumEntryId() {
		return forumEntryId;
	}

	public void setForumEntryId(Long forumEntryId) {
		this.forumEntryId = forumEntryId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
