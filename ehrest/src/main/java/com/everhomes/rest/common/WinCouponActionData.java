package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为WIN_COUPON跳转到优惠券详情并获取优惠券（参数待细化）
 * <li>couponId: 优惠券ID</li>
 * </ul>
 */
public class WinCouponActionData implements Serializable{
    private static final long serialVersionUID = -1029552301306256166L;
    //{"couponId": 1} 
    private Long couponId;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
