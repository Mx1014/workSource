package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class RegisterLoginCommand {
    @NotNull
    private Integer borderId;
    
    @NotNull
    private String loginToken;
    
    private String borderSessionId;
    
    
    
    public Integer getBorderId() {
        return borderId;
    }



    public void setBorderId(Integer borderId) {
        this.borderId = borderId;
    }



    public String getLoginToken() {
        return loginToken;
    }



    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }


    public String getBorderSessionId() {
        return borderSessionId;
    }



    public void setBorderSessionId(String borderSessionId) {
        this.borderSessionId = borderSessionId;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
