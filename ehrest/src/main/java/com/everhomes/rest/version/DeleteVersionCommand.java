// @formatter:off
package com.everhomes.rest.version;

/**
 * 
 * <ul>
 * <li>id: 记录id</li>
 * <li>urlId: url的记录id</li>
 * </ul>
 */
public class DeleteVersionCommand {
	private Long id;
	private Long urlId;

	public Long getUrlId() {
		return urlId;
	}

	public void setUrlId(Long urlId) {
		this.urlId = urlId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
