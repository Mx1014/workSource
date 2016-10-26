package com.everhomes.rest.openapi.techpark;

import java.util.List;

import com.everhomes.util.StringHelper;

public class CustomerRental {
    /** 客户名称 */
    private String name;
    
    /** 客户编号 */
    private String number;
    
    /** 客户合同，多个 */
    private List<CustomerContract> contracts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<CustomerContract> getContracts() {
        return contracts;
    }

    public void setContracts(List<CustomerContract> contracts) {
        this.contracts = contracts;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
