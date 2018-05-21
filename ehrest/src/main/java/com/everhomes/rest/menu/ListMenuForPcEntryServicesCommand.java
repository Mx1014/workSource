// @formatter:off
package com.everhomes.rest.menu;

import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class ListMenuForPcEntryServicesCommand {

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
