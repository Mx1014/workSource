package com.everhomes.dynamicExcel;

import com.everhomes.customer.EnterpriseCustomer;

public class DynamicCustomer {
    EnterpriseCustomer customer;

    String customerAddressString;

    String customerAdminString;

    public EnterpriseCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(EnterpriseCustomer customer) {
        this.customer = customer;
    }

    public String getCustomerAddressString() {
        return customerAddressString;
    }

    public void setCustomerAddressString(String customerAddressString) {
        this.customerAddressString = customerAddressString;
    }

    public String getCustomerAdminString() {
        return customerAdminString;
    }

    public void setCustomerAdminString(String customerAdminString) {
        this.customerAdminString = customerAdminString;
    }
}
