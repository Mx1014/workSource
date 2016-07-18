package com.everhomes.rest.news;

/**
 * 
 * <ul>同步新闻
 * <li>id: 新闻id，若有则同步一个，若无则同步所有</li>
 * </ul>
 */
public class SyncNewsCommand {
	Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
