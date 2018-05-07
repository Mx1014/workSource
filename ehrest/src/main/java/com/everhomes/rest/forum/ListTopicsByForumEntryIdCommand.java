package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>instanceConfig: 应用配置信息</li>
 *     <li>forumEntryId: 论坛入口Id</li>
 *     <li>tag: 论坛帖子tag</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>pageSize: pageSize</li>
 * </ul>
 */
public class ListTopicsByForumEntryIdCommand {

	private String instanceConfig;
	private Long forumEntryId;
	private String tag;
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getInstanceConfig() {
		return instanceConfig;
	}

	public void setInstanceConfig(String instanceConfig) {
		this.instanceConfig = instanceConfig;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
