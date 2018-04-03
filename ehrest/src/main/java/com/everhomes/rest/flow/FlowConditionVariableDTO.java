package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>displayName: 显示名称</li>
 *     <li>name: 传给后台的参数值</li>
 *     <li>fieldType: 字段类型 {@link com.everhomes.rest.general_approval.GeneralFormFieldType}</li>
 *     <li>operators: 运算符列表 {@link com.everhomes.rest.flow.FlowConditionRelationalOperatorType}</li>
 *     <li>options: 如果是选项的话就是选项列表</li>
 *     <li>extra: 附加选项</li>
 * </ul>
 */
public class FlowConditionVariableDTO {

    private String displayName;
    private String name;
    private String fieldType;
    private String extra;

    @ItemType(String.class)
    private List<String> operators = new ArrayList<>();
    @ItemType(String.class)
    private List<String> options = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
