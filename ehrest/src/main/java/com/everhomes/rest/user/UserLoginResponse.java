package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class UserLoginResponse {
    @ItemType(UserLoginDTO.class)
    private List<UserLoginDTO> logins;

    public List<UserLoginDTO> getLogins() {
        return logins;
    }

    public void setLogins(List<UserLoginDTO> logins) {
        this.logins = logins;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
