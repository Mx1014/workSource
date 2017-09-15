package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <ul>
 *     <li>ownerId: 组织id</li>
 *     <li>communityId: 小区id</li>
 *     <li>name: 表记名称</li>
 *     <li>meterNumber: 表记编号</li>
 *     <li>meterType: 表记类型 {@link com.everhomes.rest.energy.EnergyMeterType}</li>
 *     <li>billCategoryId: 表记项目id</li>
 *     <li>serviceCategoryId: 表记分类id</li>
 *     <li>maxReading: 最大量程</li>
 *     <li>startReading: 起始读数</li>
 *     <li>rate: 倍率</li>
 *     <li>price: 价格</li>
 *     <li>costFormulaId: 费用计算公式</li>
 *     <li>amountFormulaId: 用量计算公式</li>
 *     <li>calculationType: 价格计算方式 参考{@link com.everhomes.rest.energy.PriceCalculationType}</li>
 *     <li>configId: 价格方案id </li>
 *     <li>ownerType: 所属组织类型</li>
 *     <li>namespaceId: 域空间</li>
 * </ul>
 */
public class CreateEnergyMeterCommand {

    @NotNull private Long ownerId;
    @NotNull private Long communityId;
    @NotNull @Size(max = 100) private String name;
    @NotNull @Size(max = 50) private String meterNumber;
    @NotNull private Byte meterType;
    @NotNull private Long billCategoryId;
    @NotNull private Long serviceCategoryId;
    @NotNull private BigDecimal maxReading;
    @NotNull private BigDecimal startReading;
    @NotNull private BigDecimal rate;
    @NotNull private BigDecimal price;
    @NotNull private Long costFormulaId;
    @NotNull private Long amountFormulaId;
    private Byte calculationType;
    private Long configId;
    private String ownerType;
    private Integer namespaceId;

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Byte getMeterType() {
        return meterType;
    }

    public void setMeterType(Byte meterType) {
        this.meterType = meterType;
    }

    public BigDecimal getMaxReading() {
        return maxReading;
    }

    public void setMaxReading(BigDecimal maxReading) {
        this.maxReading = maxReading;
    }

    public BigDecimal getStartReading() {
        return startReading;
    }

    public void setStartReading(BigDecimal startReading) {
        this.startReading = startReading;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public Long getBillCategoryId() {
        return billCategoryId;
    }

    public void setBillCategoryId(Long billCategoryId) {
        this.billCategoryId = billCategoryId;
    }

    public Long getServiceCategoryId() {
        return serviceCategoryId;
    }

    public void setServiceCategoryId(Long serviceCategoryId) {
        this.serviceCategoryId = serviceCategoryId;
    }

    public Long getCostFormulaId() {
        return costFormulaId;
    }

    public void setCostFormulaId(Long costFormulaId) {
        this.costFormulaId = costFormulaId;
    }

    public Long getAmountFormulaId() {
        return amountFormulaId;
    }

    public void setAmountFormulaId(Long amountFormulaId) {
        this.amountFormulaId = amountFormulaId;
    }

    public Byte getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(Byte calculationType) {
        this.calculationType = calculationType;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
