package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>forumEntryId: 入口Id</li>
 *     <li>tag: tag</li>
 *     <li>indexFlag: 是否关联到主页签上的活动，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ForumEntryConfigulation {

	private Long forumEntryId;

	private String tag;

	private Byte indexFlag;

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

	public Byte getIndexFlag() {
		return indexFlag;
	}

	public void setIndexFlag(Byte indexFlag) {
		this.indexFlag = indexFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
