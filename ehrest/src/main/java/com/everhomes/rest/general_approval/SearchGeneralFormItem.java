package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>fieldType: 字段类型 {@link GeneralFormFieldType}</li>
 *     <li>fieldName: 字段名字</li>
 *     <li>fieldValue: 字段值</li>
 *     <li>displayName: 显示名称</li>
 *     <li>fieldAttribute: 字段属性 比如：系统字段 {@link GeneralFormFieldAttribute}</li>
 *     <li>fieldExtra: json 字符串,不同的字段属性有不同的 extra. 时间类型：{@link com.everhomes.rest.general_approval.SearchGeneralFormFieldExtra}</li>
 * </ul>
 */
public class SearchGeneralFormItem {
    private GeneralFormFieldType fieldType;
    private String fieldName;
    private String fieldValue;
    private String displayName;
    private String fieldAttribute;
    private String fieldExtra;//"{\"start\":11111111, \"end\":22222}"

    public GeneralFormFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(GeneralFormFieldType fieldType) {
        this.fieldType = fieldType;
    }

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFieldAttribute() {
        return fieldAttribute;
    }

    public void setFieldAttribute(String fieldAttribute) {
        this.fieldAttribute = fieldAttribute;
    }

    public String getFieldExtra() {
        return fieldExtra;
    }

    public void setFieldExtra(String fieldExtra) {
        this.fieldExtra = fieldExtra;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
