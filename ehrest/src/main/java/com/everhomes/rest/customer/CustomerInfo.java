package com.everhomes.rest.customer;

/**
 * Created by ying.xiong on 2018/1/17.
 */
public class CustomerInfo {

    private Long customerId;

    private Byte customerType;

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
