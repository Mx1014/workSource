package com.everhomes.rest.business;

/**
 * <ul>
 * 	<li>keyword:用户identifier或nickName模糊查找</li>
 * </ul>
 *
 */
public class ListUserByKeywordCommand {
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
