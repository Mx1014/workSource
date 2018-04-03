// @formatter:off
package com.everhomes.rest.talent;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>keyword: 关键词</li>
 * </ul>
 */
public class TalentQueryHistoryDTO {
	private Long id;
	private String keyword;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
