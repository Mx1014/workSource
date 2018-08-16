package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class RegisterWXLoginCommand {
   
    @NotNull
    private String loginToken;
    
    public String getLoginToken() {
        return loginToken;
    }


    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
