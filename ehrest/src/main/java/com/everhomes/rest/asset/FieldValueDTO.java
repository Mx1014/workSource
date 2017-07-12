package com.everhomes.rest.asset;

/**
 * <ul>
 *     <li>fieldDisplayName:字段展示名</li>
 *     <li>fieldName:字段对应数据库字段名</li>
 *     <li>fieldType:字段类型 参考{@link com.everhomes.rest.asset.FieldType}</li>
 *     <li>fieldValue:字段值</li>
 * </ul>
 */
public class FieldValueDTO {
    private String fieldDisplayName;

    private String fieldName;

    private String fieldType;

    private String fieldValue;

    public String getFieldDisplayName() {
        return fieldDisplayName;
    }

    public void setFieldDisplayName(String fieldDisplayName) {
        this.fieldDisplayName = fieldDisplayName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
