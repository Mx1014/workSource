package com.everhomes.rest.energy;

import com.everhomes.rest.energy.util.EnumType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <ul>
 *     <li>ownerId: 公式所属组织id</li>
 *     <li>ownerType: 公式所属组织类型</li>
 *     <li>communityId: 公式所属园区id</li>
 *     <li>name: 公式名称</li>
 *     <li>expression: 公式</li>
 *     <li>formulaType: 公式类型 {@link com.everhomes.rest.energy.EnergyFormulaType}</li>
 * </ul>
 */
public class CreateEnergyMeterFormulaCommand {

    @NotNull private Long ownerId;
    @NotNull private String ownerType;
    @NotNull private Long communityId;
    @NotNull @Size(max = 255) private String name;
    @NotNull @Size(max = 255) private String expression;
    @EnumType(EnergyFormulaType.class)
    private Byte formulaType;


    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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
