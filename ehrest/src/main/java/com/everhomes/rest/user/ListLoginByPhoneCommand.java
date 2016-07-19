package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class ListLoginByPhoneCommand {
    Integer namespaceId;
    String phone;
    

    
    public Integer getNamespaceId() {
        return namespaceId;
    }



    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }


    public String getPhone() {
        return phone;
    }



    public void setPhone(String phone) {
        this.phone = phone;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
