package com.everhomes.rest.rentalv2;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.admin.RentalHolidayType;
import com.everhomes.rest.rentalv2.admin.RentalOrderRuleDTO;
import com.everhomes.rest.rentalv2.admin.RentalOrderStrategy;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>ownerType: ownerType {@link com.everhomes.rest.rentalv2.RentalOwnerType}</li>
 * <li>ownerId: 园区id</li>
 * <li>resourceTypeId: 图标id</li>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>sourceType: sourceType 默认规则：default_rule， 资源规则：resource_rule{@link RuleSourceType}</li>
 * <li>sourceId: 资源id，如果是默认规则，则不填</li>
 * <li>holidayOpenFlag: holidayOpenFlag</li>
 * <li>holidayType: 节假日类型 {@link RentalHolidayType}</li>
 * <li>price: 价格</li>
 * <li>rentalType: 时间单元类型列表 {@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>dayOpenTime: 按天模式下 每天开始时间</li>
 * <li>dayCloseTime: 按天模式下 每天结束时间</li>
 * <li>refundStrategy: 退款策略 {@link RentalOrderStrategy}</li>
 * <li>overtimeStrategy: 超时加收策略 {@link RentalOrderStrategy}</li>
 * <li>refundStrategies: 退款策略列表 {@link com.everhomes.rest.rentalv2.admin.RentalOrderRuleDTO}</li>
 * <li>overtimeStrategies: 加收策略列表 {@link com.everhomes.rest.rentalv2.admin.RentalOrderRuleDTO}</li>
 * </ul>
 */
public class GetResourceRuleV2Response {

    private String ownerType;

    private Long ownerId;

    private Long resourceTypeId;

    private String resourceType;

    private String sourceType;

    private Long sourceId;

    private Byte holidayOpenFlag;
    private Byte holidayType;

    private Double dayOpenTime;
    private Double dayCloseTime;

    private Byte rentalType;
    private BigDecimal price;

    private Byte refundStrategy;
    private Byte overtimeStrategy;

    @ItemType(RentalOrderRuleDTO.class)
    private List<RentalOrderRuleDTO> refundStrategies;
    @ItemType(RentalOrderRuleDTO.class)
    private List<RentalOrderRuleDTO> overtimeStrategies;

    public Byte getRentalType() {
        return rentalType;
    }

    public void setRentalType(Byte rentalType) {
        this.rentalType = rentalType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Byte getHolidayOpenFlag() {
        return holidayOpenFlag;
    }

    public void setHolidayOpenFlag(Byte holidayOpenFlag) {
        this.holidayOpenFlag = holidayOpenFlag;
    }

    public Byte getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(Byte holidayType) {
        this.holidayType = holidayType;
    }

    public Double getDayOpenTime() {
        return dayOpenTime;
    }

    public void setDayOpenTime(Double dayOpenTime) {
        this.dayOpenTime = dayOpenTime;
    }

    public Double getDayCloseTime() {
        return dayCloseTime;
    }

    public void setDayCloseTime(Double dayCloseTime) {
        this.dayCloseTime = dayCloseTime;
    }

    public Byte getRefundStrategy() {
        return refundStrategy;
    }

    public void setRefundStrategy(Byte refundStrategy) {
        this.refundStrategy = refundStrategy;
    }

    public Byte getOvertimeStrategy() {
        return overtimeStrategy;
    }

    public void setOvertimeStrategy(Byte overtimeStrategy) {
        this.overtimeStrategy = overtimeStrategy;
    }

    public List<RentalOrderRuleDTO> getRefundStrategies() {
        return refundStrategies;
    }

    public void setRefundStrategies(List<RentalOrderRuleDTO> refundStrategies) {
        this.refundStrategies = refundStrategies;
    }

    public List<RentalOrderRuleDTO> getOvertimeStrategies() {
        return overtimeStrategies;
    }

    public void setOvertimeStrategies(List<RentalOrderRuleDTO> overtimeStrategies) {
        this.overtimeStrategies = overtimeStrategies;
    }
}
