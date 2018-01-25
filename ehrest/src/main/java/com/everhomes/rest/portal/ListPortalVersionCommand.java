// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间</li>
 *     <li>status:  {@link com.everhomes.rest.launchpad.PortalVersionStatus}</li>
 * </ul>
 */
public class ListPortalVersionCommand {

	private Integer namespaceId;
	private Byte status;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
