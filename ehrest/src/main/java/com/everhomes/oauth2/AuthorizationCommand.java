package com.everhomes.oauth2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class AuthorizationCommand {

    @NotNull
    private String responseType;

    @NotNull
    private String client_id;

    private String redirect_uri;

    private String scope;

    @NotNull
    private String state;

    public AuthorizationCommand() {
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
    
    
    public String getclient_id() {
        return client_id;
    }

    public void setclient_id(String client_id) {
        this.client_id = client_id;
    }
    
    public String getredirect_uri() {
        return redirect_uri;
    }

    public void setredirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
