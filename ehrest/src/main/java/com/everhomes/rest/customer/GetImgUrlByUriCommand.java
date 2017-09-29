package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>uri: 图片uri,如果有多个,用英文逗号分隔</li>
 * </ul>
 */
public class GetImgUrlByUriCommand {

	private String uri;
	

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
