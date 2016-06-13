// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>identifier: 标识符</li>
 * <li>errorScope: 错误码范围，不同app有不同的scope，参见不同app对应的scope说明</li>
 * <li>errorCode: 错误码</li>
 * <li>errorDescription: 错误描述</li>
 * </ul>
 */
public class CommandResult {
    private String identifier;
    
    protected String errorScope;
    
    protected Integer errorCode;
    
    protected String errorDescription;

    public CommandResult() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getErrorScope() {
        return errorScope;
    }

    public void setErrorScope(String errorScope) {
        this.errorScope = errorScope;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
