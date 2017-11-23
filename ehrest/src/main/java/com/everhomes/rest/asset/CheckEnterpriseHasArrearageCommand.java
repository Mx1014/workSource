//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/11/22.
 */

/**
 *<ul>
 * <li>namespaceId:域空间id</li>
 * <li>communityId:园区id</li>
 * <li>organizationId:企业id</li>
 *</ul>
 */
public class CheckEnterpriseHasArrearageCommand {
    private Integer namespaceId;
    private Long communityId;
    private Long organizationId;

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
}
