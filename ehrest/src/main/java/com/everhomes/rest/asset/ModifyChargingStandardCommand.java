//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>chargingStandardId:收费标准id</li>
 * <li>chargingStandardName:收费标准名称</li>
 * <li>instruction:说明</li>
 *</ul>
 */
public class ModifyChargingStandardCommand {
    private Long chargingStandardId;
    private String chargingStandardName;
    private String instruction;
    private Long ownerId;
    private String ownerType;
    private Integer namespaceId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public ModifyChargingStandardCommand() {
    }

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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }


    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public void setChargingStandardId(Long chargingStandardId) {
        this.chargingStandardId = chargingStandardId;
    }

    public String getChargingStandardName() {
        return chargingStandardName;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setChargingStandardName(String chargingStandardName) {
        this.chargingStandardName = chargingStandardName;
    }
}
