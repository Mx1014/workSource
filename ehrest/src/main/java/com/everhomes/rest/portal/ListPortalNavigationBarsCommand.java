// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>versionId: versionId</li>
 * </ul>
 */
public class ListPortalNavigationBarsCommand {

	private Long versionId;

	public Long getVersionId() {
		return versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
