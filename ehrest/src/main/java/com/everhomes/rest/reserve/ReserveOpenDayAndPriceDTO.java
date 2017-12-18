package com.everhomes.rest.reserve;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.reserve.ReserveOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>resourceType: 资源类型</li>
 * <li>resourceTypeId: 资源类型id</li>
 * <li>atMostAdvanceTime: 最多提前多少时间预定，返回毫秒</li>
 * <li>atLeastAdvanceTime: 最少提前多少时间预定，返回毫秒</li>
 * <li>dayOpenStartTime: 每天开放开始时间</li>
 * <li>dayOpenEndTime: 每天开放结束时间</li>
 * <li>holidayOpenFlag: 节假日开放预约  {@link com.everhomes.rest.reserve.ReserveCommonFlag}</li>
 * <li>holidayType: 节假日类型 {@link com.everhomes.rest.reserve.ReserveHolidayType}</li>
 * <li>timeType: 时间类型</li>
 * <li>timeUnit: 时间单元</li>
 * <li>workdayPrice: 工作日价格</li>
 * <li>holidayPrice: 节假日价格</li>
 * <li>autoAssign: 是否自动分配</li>
 * <li>multiFlag: 是否支持多选 {@link com.everhomes.rest.reserve.ReserveCommonFlag}</li>
 * </ul>
 */
public class ReserveOpenDayAndPriceDTO {

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String resourceType;
    private Long resourceTypeId;
    private Long atMostAdvanceTime;
    private Long atLeastAdvanceTime;
    private Double dayOpenStartTime;
    private Double dayOpenEndTime;
    private Byte holidayOpenFlag;
    private Byte holidayType;
    private Byte timeType;
    private Integer timeUnit;
    private BigDecimal workdayPrice;
    private BigDecimal holidayPrice;

    private Byte autoAssign;
    private Byte multiFlag;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public Long getAtMostAdvanceTime() {
        return atMostAdvanceTime;
    }

    public void setAtMostAdvanceTime(Long atMostAdvanceTime) {
        this.atMostAdvanceTime = atMostAdvanceTime;
    }

    public Long getAtLeastAdvanceTime() {
        return atLeastAdvanceTime;
    }

    public void setAtLeastAdvanceTime(Long atLeastAdvanceTime) {
        this.atLeastAdvanceTime = atLeastAdvanceTime;
    }

    public Double getDayOpenStartTime() {
        return dayOpenStartTime;
    }

    public void setDayOpenStartTime(Double dayOpenStartTime) {
        this.dayOpenStartTime = dayOpenStartTime;
    }

    public Double getDayOpenEndTime() {
        return dayOpenEndTime;
    }

    public void setDayOpenEndTime(Double dayOpenEndTime) {
        this.dayOpenEndTime = dayOpenEndTime;
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

    public Byte getTimeType() {
        return timeType;
    }

    public void setTimeType(Byte timeType) {
        this.timeType = timeType;
    }

    public Integer getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }

    public BigDecimal getWorkdayPrice() {
        return workdayPrice;
    }

    public void setWorkdayPrice(BigDecimal workdayPrice) {
        this.workdayPrice = workdayPrice;
    }

    public BigDecimal getHolidayPrice() {
        return holidayPrice;
    }

    public void setHolidayPrice(BigDecimal holidayPrice) {
        this.holidayPrice = holidayPrice;
    }

    public Byte getAutoAssign() {
        return autoAssign;
    }

    public void setAutoAssign(Byte autoAssign) {
        this.autoAssign = autoAssign;
    }

    public Byte getMultiFlag() {
        return multiFlag;
    }

    public void setMultiFlag(Byte multiFlag) {
        this.multiFlag = multiFlag;
    }
}
