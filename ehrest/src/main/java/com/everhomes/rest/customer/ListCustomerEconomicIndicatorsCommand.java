package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId：所属客户id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/24.
 */
public class ListCustomerEconomicIndicatorsCommand {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
