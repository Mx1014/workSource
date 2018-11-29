package com.everhomes.rest.officecubicle;

/**
 * <ul>
 * <li>durationType: 1: 时长内, 2: 时长外 {@link RentalDurationType}</li>
 * <li>durationUnit: 时长单位，1:小时 {@link RentalDurationUnit}</li>
 * <li>duration: 时长</li>
 * <li>factor: 价格系数</li>
 * </ul>
 */
public class OfficeCubicleRefundRuleDTO {

    private Byte durationType;
    private Byte durationUnit;
    private Double duration;
    private Double factor;


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
