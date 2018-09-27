// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>communityIds: communityIds列表</li>
 *     <li>fromOrgId: 原属管理公司Id</li>
 *     <li>toOrgId: 新的管理公司Id</li>
 * </ul>
 */
public class ChangeOrganizationCommunitiesCommand {

    private List<Long> communityIds;
    private Long fromOrgId;
    private Long toOrgId;

    public List<Long> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(List<Long> communityIds) {
        this.communityIds = communityIds;
    }

    public Long getFromOrgId() {
        return fromOrgId;
    }

    public void setFromOrgId(Long fromOrgId) {
        this.fromOrgId = fromOrgId;
    }

    public Long getToOrgId() {
        return toOrgId;
    }

    public void setToOrgId(Long toOrgId) {
        this.toOrgId = toOrgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
