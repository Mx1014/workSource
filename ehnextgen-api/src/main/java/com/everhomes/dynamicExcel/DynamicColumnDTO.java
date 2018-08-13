//@formatter:off
package com.everhomes.dynamicExcel;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2018/1/18.
 */

public class DynamicColumnDTO implements  Cloneable {
    /**
     * fieldName 字段的逻辑值
     */
    private String fieldName;

    private Long fieldId;
    /**
     * @param value 给定行号和列号的数据，已经转为了string
     */
    private String value;
    /**
     * @param headerDisplay 对应的标题名称
     */
    private String headerDisplay;
    /**
     * @param columnNum 列号
     */
    private Integer columnNum;

    //必填的是否填写
    private boolean mandatoryFlag = true;

    public String getValue() {
        return value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHeaderDisplay() {
        return headerDisplay;
    }

    public void setHeaderDisplay(String headerDisplay) {
        this.headerDisplay = headerDisplay;
    }

    public Integer getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(Integer columnNum) {
        this.columnNum = columnNum;
    }

    public boolean getMandatoryFlag() {
        return mandatoryFlag;
    }

    public void setMandatoryFlag(boolean mandatoryFlag) {
        this.mandatoryFlag = mandatoryFlag;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public boolean isMandatoryFlag() {
        return mandatoryFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
