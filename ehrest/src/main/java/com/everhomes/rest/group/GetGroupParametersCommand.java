// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * </ul>
 */
public class GetGroupParametersCommand {

	private Integer namespaceId;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
