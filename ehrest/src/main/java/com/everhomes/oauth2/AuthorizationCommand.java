package com.everhomes.oauth2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class AuthorizationCommand {

    @NotNull
    private String responseType;

    @NotNull
    private String clientId;

    private String redirectUri;

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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
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
