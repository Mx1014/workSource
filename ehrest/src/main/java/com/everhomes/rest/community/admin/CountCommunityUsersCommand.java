package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * namespaceId: 命名空间
 *
 */
public class CountCommunityUsersCommand {
	
	private Integer namespaceId;
	
	private Long communityId;
	
	private Long organizationId;

	private Byte statisticsType;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	
	
	
	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Byte getStatisticsType() {
		return statisticsType;
	}

	public void setStatisticsType(Byte statisticsType) {
		this.statisticsType = statisticsType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
