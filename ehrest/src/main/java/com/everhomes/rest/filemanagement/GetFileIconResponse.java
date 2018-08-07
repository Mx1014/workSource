// @formatter:off
package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>iconUrl: 图标url</li>
 * </ul>
 */
public class GetFileIconResponse {

	private String iconUrl;

	public GetFileIconResponse() {

	}

	public GetFileIconResponse(String iconUrl) {
		super();
		this.iconUrl = iconUrl;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
