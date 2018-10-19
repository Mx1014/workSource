// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 企业ID</li>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>communityId: 项目ID</li>
 * </ul>
 */
public class GetUserAuthenticationOrganizationCommand {

    private Long organizationId;

    private Integer namespaceId;

    private Long communityId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
