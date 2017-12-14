package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>point: 积分 {@link com.everhomes.rest.user.UserTreasureDTO}</li>
 *     <li>coupon: 礼券 {@link com.everhomes.rest.user.UserTreasureDTO}</li>
 *     <li>order: 订单 {@link com.everhomes.rest.user.UserTreasureDTO}</li>
 * </ul>
 */
public class GetUserTreasureResponse {

    private UserTreasureDTO point;
    private UserTreasureDTO coupon;
    private UserTreasureDTO order;

    public UserTreasureDTO getPoint() {
        return point;
    }

    public void setPoint(UserTreasureDTO point) {
        this.point = point;
    }

    public UserTreasureDTO getCoupon() {
        return coupon;
    }

    public void setCoupon(UserTreasureDTO coupon) {
        this.coupon = coupon;
    }

    public UserTreasureDTO getOrder() {
        return order;
    }

    public void setOrder(UserTreasureDTO order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
