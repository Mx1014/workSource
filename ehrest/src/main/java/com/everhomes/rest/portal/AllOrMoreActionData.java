// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>type: 类型</li>
 * <li>align: 居左或居中</li>
 * <li>defUri: 默认icon</li>
 * </ul>
 */
public class AllOrMoreActionData {

	private String type;

	private String align;

	private String defUri;

	private String defUrl;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getDefUri() {
		return defUri;
	}

	public void setDefUri(String defUri) {
		this.defUri = defUri;
	}

	public String getDefUrl() {
		return defUrl;
	}

	public void setDefUrl(String defUrl) {
		this.defUrl = defUrl;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
