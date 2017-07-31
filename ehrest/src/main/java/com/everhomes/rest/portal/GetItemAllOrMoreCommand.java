// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间</li>
 * <li>moreOrAllType: 更多或者全部类型： all(全部),more(更多)</li>
 * </ul>
 */
public class GetItemAllOrMoreCommand {

	private Integer namespaceId;

	private String moreOrAllType;

	public GetItemAllOrMoreCommand() {

	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getMoreOrAllType() {
		return moreOrAllType;
	}

	public void setMoreOrAllType(String moreOrAllType) {
		this.moreOrAllType = moreOrAllType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
