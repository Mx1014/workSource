package com.everhomes.rest.locale;

import javax.validation.constraints.NotNull;

public class GetLocalizedStringCommand {
    
    @NotNull
    private String scope;
    
    @NotNull
    private String code;
    
    private String locale;
    
    private String defaultValue;
    
    public GetLocalizedStringCommand() {
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
