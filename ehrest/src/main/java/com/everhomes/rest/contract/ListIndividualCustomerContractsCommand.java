package com.everhomes.rest.contract;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 *     <li>individualCustomerId: 个人客户id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/26.
 */
public class ListIndividualCustomerContractsCommand {
    private Long individualCustomerId;

    private Long communityId;

    private Integer namespaceId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getIndividualCustomerId() {
        return individualCustomerId;
    }

    public void setIndividualCustomerId(Long individualCustomerId) {
        this.individualCustomerId = individualCustomerId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
