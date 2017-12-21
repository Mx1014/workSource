package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>customerType: 客户类型 0: enterprise; 1: individual</li>
 *     <li>customerId: 客户id</li>
 *     <li>communityId: 项目id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class ListCustomerTaxesCommand {

    private Byte customerType;

    private Long customerId;

    private Long communityId;

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
}
