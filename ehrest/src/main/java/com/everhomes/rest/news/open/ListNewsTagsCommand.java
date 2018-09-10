package com.everhomes.rest.news.open;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>categoryId: 标识所属快讯应用</li>
 * </ul>
 */
public class ListNewsTagsCommand {

	private Long categoryId;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

}
