package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>formulaId: 公式id</li>
 * </ul>
 */
public class DeleteEnergyMeterFormulaCommand {

    @NotNull private Long organizationId;
    @NotNull private Long formulaId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getFormulaId() {

        return formulaId;
    }

    public void setFormulaId(Long formulaId) {
        this.formulaId = formulaId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
