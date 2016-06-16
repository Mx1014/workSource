package com.everhomes.rest.news;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 查询新闻正文
 * <li>id: 新闻id</li>
 * </ul>
 */
public class QueryNewsContentCommand {
	@NotNull
	private Long id;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}