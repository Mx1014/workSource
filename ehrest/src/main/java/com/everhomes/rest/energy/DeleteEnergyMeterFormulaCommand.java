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

    private Long communityId;
    @NotNull private Long organizationId;
    @NotNull private Long formulaId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

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
