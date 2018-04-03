package com.everhomes.energy;

/**
 * Created by ying.xiong on 2017/10/27.
 */
public class PlanMeter {
    private Long planId;
    private Long meterId;
    private Long repeatSettingId;

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getRepeatSettingId() {
        return repeatSettingId;
    }

    public void setRepeatSettingId(Long repeatSettingId) {
        this.repeatSettingId = repeatSettingId;
    }
}
