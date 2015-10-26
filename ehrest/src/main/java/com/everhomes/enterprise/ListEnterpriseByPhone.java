package com.everhomes.enterprise;

import javax.validation.constraints.NotNull;

public class ListEnterpriseByPhone {
    @NotNull
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
}
