package com.everhomes.discover;

/**
 * Specifies REST API parameter meta information
 * 
 * @author Kelven Yang
 *
 */
public class RestParam {
    private String paramName;
    private String typeName;
    private boolean required;
    private boolean pathVariable;
    private String description;
    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPathVariable() {
        return pathVariable;
    }

    public void setPathVariable(boolean pathVariable) {
        this.pathVariable = pathVariable;
    }
}
