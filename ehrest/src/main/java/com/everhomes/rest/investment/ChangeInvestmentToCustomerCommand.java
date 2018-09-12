package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.util.List;

public class ChangeInvestmentToCustomerCommand {

    private List<Long> customerIds;


    public List<Long> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Long> customerIds) {
        this.customerIds = customerIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
