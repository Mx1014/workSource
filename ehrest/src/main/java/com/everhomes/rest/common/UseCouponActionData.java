package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为USE COUPON时跳转到优惠券详情并使用优惠券（参数待细化）
 * <li>couponId: 优惠券id</li>
 * </ul>
 */
public class UseCouponActionData implements Serializable{

    private static final long serialVersionUID = -1513899386773142335L;
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
