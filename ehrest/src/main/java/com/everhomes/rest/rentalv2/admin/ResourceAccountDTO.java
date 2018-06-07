package com.everhomes.rest.rentalv2.admin;

import com.everhomes.rest.order.ListBizPayeeAccountDTO;

public class ResourceAccountDTO {

    private Long id;
    private String resourceName;
    private ListBizPayeeAccountDTO account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public ListBizPayeeAccountDTO getAccount() {
        return account;
    }

    public void setAccount(ListBizPayeeAccountDTO account) {
        this.account = account;
    }
}
