// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>url: url</li>
 * <li>declareFlag: 是否声明</li>
 * </ul>
 */
public class UrlActionData {

	private String url;

	private Byte declareFlag;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Byte getDeclareFlag() {
		return declareFlag;
	}

	public void setDeclareFlag(Byte declareFlag) {
		this.declareFlag = declareFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
