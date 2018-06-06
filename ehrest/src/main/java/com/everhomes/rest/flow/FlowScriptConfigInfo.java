package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>fieldName: 配置name</li>
 *     <li>fieldValue: 配置value</li>
 *     <li>fieldDesc: fieldDesc</li>
 * </ul>
 */
public class FlowScriptConfigInfo {

    private String fieldName;
    private String fieldValue;
    private String fieldDesc;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
