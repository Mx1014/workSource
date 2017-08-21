package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>fieldGroupId: 所属分组 id</li>
 * <li>fieldName: 字段名称</li>
 * <li>fieldHint: 字段提示</li>
 * <li>fieldType: 字段类型</li>
 * <li>fieldValue: 字段附加值</li>
 * </ul>
 */
public class AddArchivesFieldCommand {

    private Long fieldGroupId;

    private String fieldName;

    private String fieldHint;

    private Byte fieldType;

    private String fieldValue;

    public AddArchivesFieldCommand() {
    }

    public Long getFieldGroupId() {
        return fieldGroupId;
    }

    public void setFieldGroupId(Long fieldGroupId) {
        this.fieldGroupId = fieldGroupId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldHint() {
        return fieldHint;
    }

    public void setFieldHint(String fieldHint) {
        this.fieldHint = fieldHint;
    }

    public Byte getFieldType() {
        return fieldType;
    }

    public void setFieldType(Byte fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
