package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId:域空间id</li>
 * <li>appId: 应用Id</li>
 * <li>communityId</li>
 * </ul>
 */
public class ListCitiesByOrgIdAndCommunitIdCommand {
	private Integer namespaceId;
	private Long appId;
	private Long communityId;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

}
