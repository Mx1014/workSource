package com.everhomes.rest.oauth2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class AuthorizationCommand {

    @NotNull
    private String response_type;

    @NotNull
    private String client_id;

    private String redirect_uri;

    private String scope;

    @NotNull
    private String state;

    public AuthorizationCommand() {
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

    public String getresponse_type() {
        return response_type;
    }

    public void setresponse_type(String response_type) {
        this.response_type = response_type;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
