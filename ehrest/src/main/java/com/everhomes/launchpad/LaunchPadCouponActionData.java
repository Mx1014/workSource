package com.everhomes.launchpad;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为coupon时点击item需要的参数
 * <li>categoryId: 优惠券类型id</li>
 * </ul>
 */
public class LaunchPadCouponActionData implements Serializable{

    private static final long serialVersionUID = 4810470677136730093L;
    //{"categoryId": 1}  
    private Integer categoryId;
    
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
