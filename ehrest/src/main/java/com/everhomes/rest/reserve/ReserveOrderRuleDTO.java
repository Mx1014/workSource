package com.everhomes.rest.reserve;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>ownerType: 规则类型 default：默认，resource：某一个资源的使用规则  {@link com.everhomes.rest.reserve.ReserveRuleOwnerType}</li>
 * <li>ownerId: 对应ownerType的id</li>
 * <li>strategyType: 1: 退款, 2: 加收  {@link ReserveOrderHandleType}</li>
 * <li>durationType: 1: 时长内, 2: 时长外 {@link com.everhomes.rest.reserve.ReserveRuleDurationType}</li>
 * <li>durationUnit: 时长单位，1:小时 2:天 {@link com.everhomes.rest.reserve.ReserveRuleDurationUnit}</li>
 * <li>duration: 时长</li>
 * <li>factor: 价格系数</li>
 * </ul>
 */
public class ReserveOrderRuleDTO {

    private Long id;
    private String ownerType;
    private Long ownerId;
    private Byte strategyType;
    private Byte durationType;
    private String durationUnit;
    private Double duration;
    private Double factor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Byte getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(Byte strategyType) {
        this.strategyType = strategyType;
    }

    public Byte getDurationType() {
        return durationType;
    }

    public void setDurationType(Byte durationType) {
        this.durationType = durationType;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
