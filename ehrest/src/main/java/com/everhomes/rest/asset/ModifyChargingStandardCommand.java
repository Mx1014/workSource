//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>chargingStandardId:收费标准id</li>
 * <li>chargingStandardName:收费标准名称</li>
 * <li>instruction:说明</li>
 * <li>suggestUnitPrice:建议单价</li>
 * <li>areaSizeType:计费面积类型</li>
 *</ul>
 */
public class ModifyChargingStandardCommand {
    private Long chargingStandardId;
    private String chargingStandardName;
    private String instruction;
    private BigDecimal suggestUnitPrice;
    private Integer areaSizeType;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public ModifyChargingStandardCommand() {
    }

    public BigDecimal getSuggestUnitPrice() {
        return suggestUnitPrice;
    }

    public void setSuggestUnitPrice(BigDecimal suggestUnitPrice) {
        this.suggestUnitPrice = suggestUnitPrice;
    }

    public Integer getAreaSizeType() {
        return areaSizeType;
    }

    public void setAreaSizeType(Integer areaSizeType) {
        this.areaSizeType = areaSizeType;
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
