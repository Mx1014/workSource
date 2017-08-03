package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class ImportEnterpriseCustomerDataCommand {

    private Integer namespaceId;

    private Long communityId;

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
}
