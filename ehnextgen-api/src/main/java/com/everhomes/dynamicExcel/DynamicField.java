//@formatter:off
package com.everhomes.dynamicExcel;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/11.
 */

public class DynamicField {
    /**
     * fieldName:字段的名称
     */
    private String fieldName;

    private String fieldParam;

    private Long fieldId;
    @NotNull
    /**
     * displayName: 字段在excel上展示的名字
     */
    private String displayName;
    private String defaultValue = "";
    private boolean isMandatory = false;
    private List<String> allowedValued;
    //字段的日期格式
    private String dateFormat = "yyyy-MM-dd";

    private Long groupId;

    public String getDateFormat() {
        return dateFormat;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<String> getAllowedValued() {
        return allowedValued;
    }

    public void setAllowedValued(List<String> allowedValued) {
        this.allowedValued = allowedValued;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFieldParam() {
        return fieldParam;
    }

    public void setFieldParam(String fieldParam) {
        this.fieldParam = fieldParam;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
