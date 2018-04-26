// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>versionId: versionId</li>
 *     <li>userIds: userIds</li>
 * </ul>
 */
public class UpdatePortalVersionUsersCommand {


	private Integer namespaceId;
	private Long versionId;
	private List<Long> userIds;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getVersionId() {
		return versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
