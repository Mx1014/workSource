package com.everhomes.rest.community.admin;


import java.util.List;

/**
 * <ul>
 *     <li>communityIds: 小区id</li>
 *     <li>namespaceId: 域空间ID</li>
 * </ul>
 */
public class ExportBatchCommunityUsersCommand {

	private List<Long> communityIds;

	private Integer namespaceId;

	public List<Long> getCommunityIds() {
		return communityIds;
	}

	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
	}

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
