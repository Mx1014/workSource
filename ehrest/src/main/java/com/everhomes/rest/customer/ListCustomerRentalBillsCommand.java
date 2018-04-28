package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId：所属客户id</li>
 *     <li>communityId：所属项目id</li>
 *     <li>orgId：物业公司id</li>
 *     <li>namespaceId：域空间id</li>
 *     <li>pageAnchor：锚点</li>
 *     <li>pageSize：页面大小</li>
 * </ul>
 * Created by ying.xiong on 2018/4/8.
 */
public class ListCustomerRentalBillsCommand {
    private Long customerId;

    private Byte customerType;

    private Long communityId;

    private Long orgId;

    private Integer namespaceId;

    private Long pageAnchor;
    private Integer pageSize;

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
