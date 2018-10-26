//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 *<ul>
 * <li>namespaceId:域空间id</li>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者类型</li>
 * <li>chargingItemId:收费项目id</li>
 * <li>chargingStandardName:收费标准名称</li>
 * <li>formulaType:公式类型,1:固定金额;2:普通公式;3:斜率跟着变量区间总体变化(斜面);4:斜率在不同变量区间取值不同（阶梯）（楼梯）;</li>
 * <li>billingCycle:计费周期,参考{@link com.everhomes.rest.asset.BillingCycle}</li>
 * <li>formulaJson:公式的json</li>
 * <li>formula:公式</li>
 * <li>instruction:说明</li>
 * <li>suggestUnitPrice:建议单价</li>
 * <li>areaSizeType:计费面积类型,1：合同面积；2.建筑面积；3：使用面积；4：出租面积</li>
 * <li>categoryId: genious creation</li>
 * <li>organizationId: 管理公司id</li>
 * <li>useUnitPrice: 是否启用填单价，1：启用；0：不启用</li>
 *</ul>
 */
public class CreateChargingStandardCommand {
    @NotNull
    private Integer namespaceId;
    @NotNull
    private Long ownerId;
    @NotNull
    private String ownerType;
    @NotNull
    private String chargingStandardName;
    @NotNull
    private Long chargingItemId;
    @NotNull
    private Byte formulaType;
    @NotNull
    private Byte billingCycle;
    private String formula;
    @NotNull
    private String formulaJson;
    private String instruction;

    private BigDecimal suggestUnitPrice;
    private Integer areaSizeType;

    @ItemType(VariableConstraints.class)
    private List<VariableConstraints> stepValuePairs;
    private Long chargingStandardId;
    private Long categoryId;

    private Long organizationId;

    private Long appId;
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    private Byte useUnitPrice;

    public Byte getUseUnitPrice() {
        return useUnitPrice;
    }

    public void setUseUnitPrice(Byte useUnitPrice) {
        this.useUnitPrice = useUnitPrice;
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

    public List<VariableConstraints> getStepValuePairs() {
        return stepValuePairs;
    }

    public void setStepValuePairs(List<VariableConstraints> stepValuePairs) {
        this.stepValuePairs = stepValuePairs;
    }

    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public void setChargingStandardId(Long chargingStandardId) {
        this.chargingStandardId = chargingStandardId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getChargingItemId() {
        return chargingItemId;
    }

    public void setChargingItemId(Long chargingItemId) {
        this.chargingItemId = chargingItemId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public String getChargingStandardName() {
        return chargingStandardName;
    }

    public void setChargingStandardName(String chargingStandardName) {
        this.chargingStandardName = chargingStandardName;
    }

    public Byte getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(Byte formulaType) {
        this.formulaType = formulaType;
    }

    public Byte getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Byte billingCycle) {
        this.billingCycle = billingCycle;
    }

    public String getFormulaJson() {
        return formulaJson;
    }

    public void setFormulaJson(String formulaJson) {
        this.formulaJson = formulaJson;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public CreateChargingStandardCommand() {

    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
