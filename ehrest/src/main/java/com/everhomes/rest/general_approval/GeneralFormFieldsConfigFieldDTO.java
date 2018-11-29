package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>fieldName：字段名称</li>
 * </ul>
 * @author huqi
 */
public class GeneralFormFieldsConfigFieldDTO {
    private String fieldName;

    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
