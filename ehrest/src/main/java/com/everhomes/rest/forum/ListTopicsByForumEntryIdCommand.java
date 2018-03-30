package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>forumEntryId: 论坛入口Id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>pageSize: pageSize</li>
 * </ul>
 */
public class ListTopicsByForumEntryIdCommand {

	private Long forumEntryId;
	private Integer namespaceId;
	private Long pageSize;

	public Long getForumEntryId() {
		return forumEntryId;
	}

	public void setForumEntryId(Long forumEntryId) {
		this.forumEntryId = forumEntryId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
