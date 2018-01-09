package com.everhomes.rest.rentalv2.admin;

/**
 * <ul>
 * <li>id: id</li>
 * <li>handleType: 1: 退款, 2: 加收  {@link RentalOrderHandleType}</li>
 * <li>durationType: 1: 时长内, 2: 时长外 {@link RentalDurationType}</li>
 * <li>durationUnit: 时长单位，1:小时 {@link RentalDurationUnit}</li>
 * <li>duration: 时长</li>
 * <li>factor: 价格系数</li>
 * </ul>
 */
public class RentalOrderRuleDTO {
    private Long id;
    private Byte handleType;
    private Byte durationType;
    private Byte durationUnit;
    private Double duration;
    private Double factor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getHandleType() {
        return handleType;
    }

    public void setHandleType(Byte handleType) {
        this.handleType = handleType;
    }

    public Byte getDurationType() {
        return durationType;
    }

    public void setDurationType(Byte durationType) {
        this.durationType = durationType;
    }

    public Byte getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(Byte durationUnit) {
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

}
