package com.everhomes.rest.energy;

import com.everhomes.rest.energy.util.EnumType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>name: 公式名称</li>
 *     <li>expression: 公式</li>
 *     <li>formulaType: 公式类型 {@link com.everhomes.rest.energy.EnergyFormulaType}</li>
 * </ul>
 */
public class CreateEnergyMeterFormulaCommand {

    @NotNull private Long organizationId;
    @NotNull @Size(max = 255) private String name;
    @NotNull @Size(max = 255) private String expression;
    @EnumType(EnergyFormulaType.class)
    private Byte formulaType;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getFormulaType() {
        return formulaType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setFormulaType(Byte formulaType) {
        this.formulaType = formulaType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
