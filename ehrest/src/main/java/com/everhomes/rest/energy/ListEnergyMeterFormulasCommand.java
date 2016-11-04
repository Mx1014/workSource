package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>formulaType: 公式类型 {@link com.everhomes.rest.energy.EnergyFormulaType}</li>
 * </ul>
 */
public class ListEnergyMeterFormulasCommand {

    @NotNull private Long organizationId;
    @NotNull private Byte formulaType;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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
