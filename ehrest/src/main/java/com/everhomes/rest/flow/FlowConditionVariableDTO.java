package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>name: 显示名称</li>
 *     <li>value: 传给后台的参数值</li>
 *     <li>fieldType: 字段类型 {@link com.everhomes.rest.general_approval.GeneralFormFieldType}</li>
 *     <li>operators: 运算符列表</li>
 *     <li>options: 如果是选项的话就是选项列表</li>
 * </ul>
 */
public class FlowConditionVariableDTO {

    private String name;
    private String value;
    private String fieldType;

    private List<String> operators = new ArrayList<>();
    private List<String> options = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public List<String> getOperators() {
        return operators;
    }

    public void setOperators(List<String> operators) {
        this.operators = operators;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
