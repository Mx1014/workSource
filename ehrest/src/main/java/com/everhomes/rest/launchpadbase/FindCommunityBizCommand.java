// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 默认配置时使用organizationId</li>
 *     <li>communityId: 园区独立配置时使用communityId，客户端仅传communityId</li>
 * </ul>
 */
public class FindCommunityBizCommand {

    private Long organizationId;

    private Long communityId;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
