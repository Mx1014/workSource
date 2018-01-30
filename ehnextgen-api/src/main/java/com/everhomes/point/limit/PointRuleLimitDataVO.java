package com.everhomes.point.limit;

/**
 * Created by xq.tian on 2017/12/8.
 */
public class PointRuleLimitDataVO {

    private Integer times;
    private Byte disabled;

    public Byte getDisabled() {
        return disabled;
    }

    public void setDisabled(Byte disabled) {
        this.disabled = disabled;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
