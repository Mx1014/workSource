package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.util.List;

public class ChangeInvestmentToCustomerCommand {

    private List<Long> customerId;


    public List<Long> getCustomerId() {
        return customerId;
    }

    public void setCustomerId(List<Long> customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
