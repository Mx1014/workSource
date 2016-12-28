package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>activityId: 活动id</li>
 *     <li>goodId: 物品id</li>
 * </ul>
 */
public class GetActivityGoodsCommand {

    private Long activityId;

    private Long goodId;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
