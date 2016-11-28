package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 公式id</li>
 *     <li>name: 公式名称</li>
 *     <li>expression: 公式</li>
 *     <li>formulaType: 公式类型 {@link com.everhomes.rest.energy.EnergyFormulaType}</li>
 * </ul>
 */
public class EnergyMeterFormulaDTO {

    private Long id;
    private String name;
    private String expression;
    private Byte formulaType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Byte getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(Byte formulaType) {
        this.formulaType = formulaType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
