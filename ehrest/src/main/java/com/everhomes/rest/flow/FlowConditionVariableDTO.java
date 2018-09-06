package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>displayName: 显示名称</li>
 *     <li>value: 传给后台的参数值</li>
 *     <li>fieldType: 字段类型 {@link com.everhomes.rest.general_approval.GeneralFormFieldType}</li>
 *     <li>operators: 运算符列表 {@link com.everhomes.rest.flow.FlowConditionRelationalOperatorType}</li>
 *     <li>options: 此字段以过期, 见 optionTuples</li>
 *     <li>optionTuples: 如果是选项的话就是选项列表</li>
 *     <li>extra: 附加信息</li>
 * </ul>
 */
public class FlowConditionVariableDTO {

    private String displayName;
    private String value;
    private String fieldType;
    private String extra;

    @ItemType(String.class)
    private List<String> operators;

    /**
     * @see FlowConditionVariableDTO#optionTuples
     */
    @Deprecated
    @JsonIgnore
    private List<String> options;

    private List<FlowConditionVariableOption> optionTuples;

    public FlowConditionVariableDTO() {
        operators = new ArrayList<>();
        options = new ArrayList<>();
        optionTuples = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String name) {
        this.value = name;
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

    /**
     * @see FlowConditionVariableDTO#getOptionTuples
     */
    @Deprecated
    public List<String> getOptions() {
        return options;
    }

    /**
     * @see FlowConditionVariableDTO#setOptionTuples
     */
    @Deprecated
    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public List<FlowConditionVariableOption> getOptionTuples() {
        return optionTuples;
    }

    public void setOptionTuples(List<FlowConditionVariableOption> optionTuples) {
        this.optionTuples = optionTuples;
    }

    public void optionsToOptionTuples() {
        if (this.options != null && this.options.size() > 0) {
            List<FlowConditionVariableOption> ops = new ArrayList<>(options.size());
            for (String option : this.options) {
                ops.add(new FlowConditionVariableOption(option, option));
            }
            if (this.optionTuples == null) {
                this.optionTuples = ops;
            } else {
                this.optionTuples.addAll(ops);
            }
        }
        if (this.optionTuples != null && this.optionTuples.size() == 0) {
            this.optionTuples = null;
        }
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
