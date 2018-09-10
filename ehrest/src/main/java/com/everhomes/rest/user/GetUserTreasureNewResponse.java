package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>point: 积分数量 </li>
 *     <li>coupon: 礼券数量 </li>
 *     <li>order: 订单数量 </li>
 * </ul>
 */
public class GetUserTreasureNewResponse {

    private Long point;
    private Long coupon;
    private Long order;

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public Long getCoupon() {
        return coupon;
    }

    public void setCoupon(Long coupon) {
        this.coupon = coupon;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
