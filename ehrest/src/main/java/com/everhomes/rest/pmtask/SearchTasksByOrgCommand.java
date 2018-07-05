//@formatter:off
package com.everhomes.rest.pmtask;

/**
 * Created by Wentian Wang on 2018/6/6.
 */

public class SearchTasksByOrgCommand {
    private Long communityId;
    private Long organizationId;
    private Integer namespaceId;

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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
