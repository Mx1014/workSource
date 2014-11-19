package com.everhomes.discover;

public class RestParam {
    private String paramName;
    private String typeName;
    private boolean required;
    
    public RestParam() {
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public boolean getRequired() {
        return this.required;
    }
    
    public void setRequired(boolean required) {
        this.required = required;
    }
 }
