package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 组织ID</li>
 *     <li>workPlaceName: 办公地点</li>
 *     <li>communityId: 项目编号</li>
 * </ul>
 */
public class DeleteWorkPlacesCommand {

    private Long organizationId;

    private String siteName;

    private Long communityId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
