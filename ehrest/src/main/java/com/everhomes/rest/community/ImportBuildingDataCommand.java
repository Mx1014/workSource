package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id，用于权限校验</li>
 * <li>organizationId: 管理公司id，用于权限校验</li>
 * <li>communityId: 园区id，用于权限校验</li>
 * </ul>
 */
public class ImportBuildingDataCommand {
	
	private Long communityId;
    private Integer namespaceId;
    private Long organizationId;

    public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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
