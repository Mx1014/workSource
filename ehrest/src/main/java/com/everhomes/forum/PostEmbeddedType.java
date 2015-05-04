// @formatter:off
package com.everhomes.forum;

/**
 * <p>帖子内嵌对象类型:</p>
 * <ul>
 * <li>ACTIVITY: 小活动</li>
 * <li>POLLING: 投票</li>
 * <li>COUPON: 优惠券</li>
 * </ul>
 */
public enum PostEmbeddedType {
    ACTIVITY("activity"), POLLING("polling"), COUPON("coupon"), ;
    //1-小活动、2-投票、3-课程、4-房屋租售、5-二手交易、6-圈名片、7-优惠券
    private String code;
    private PostEmbeddedType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PostEmbeddedType fromCode(String code) {
        if(code == null)
            return null;
        
        if(code.equalsIgnoreCase("activity"))
            return ACTIVITY;
        else if(code.equalsIgnoreCase("polling"))
            return POLLING;
        else if(code.equalsIgnoreCase("coupon"))
        	return COUPON;
        
        return null;
    }
}
