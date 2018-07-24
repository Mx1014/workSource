package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>pass: pass</li>
 *     <li>description: description</li>
 *     <li>fieldName: 配置name</li>
 * </ul>
 */
public class FlowScriptConfigValidateResult {

    private Boolean pass;
    private String description;
    private String fieldName;

    public Boolean isPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
