package com.everhomes.rest.contract;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 *     <li>enterpriseCustomerId: 企业客户id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/22.
 */
public class ListEnterpriseCustomerContractsCommand {
    private Long enterpriseCustomerId;

    private Long communityId;

    private Integer namespaceId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getEnterpriseCustomerId() {
        return enterpriseCustomerId;
    }

    public void setEnterpriseCustomerId(Long enterpriseCustomerId) {
        this.enterpriseCustomerId = enterpriseCustomerId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
