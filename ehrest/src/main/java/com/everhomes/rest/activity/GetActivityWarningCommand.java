// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * </ul>
 */
public class GetActivityWarningCommand {
	private Integer namespaceId;
	
	public GetActivityWarningCommand() {
		super();
	}

	public GetActivityWarningCommand(Integer namespaceId) {
		super();
		this.namespaceId = namespaceId;
	}

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
