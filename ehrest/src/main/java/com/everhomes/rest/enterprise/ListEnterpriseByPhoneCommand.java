package com.everhomes.rest.enterprise;

import javax.validation.constraints.NotNull;

public class ListEnterpriseByPhoneCommand {
    @NotNull
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
}
