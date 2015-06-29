// @formatter:off
package com.everhomes.banner;

/**
 * <ul>banner的跳转参数类型
 * <li>NONE: 默认</li>
 * <li>ZUOLIN_URL: 左邻官方url</li>
 * <li>THIRDPART_URL: 第三方url</li>
 * <li>POST: 帖子</li>
 * <li>COUPON: 优惠券</li>
 * </ul>
 */
public enum ActionType {
    NONE((byte)0),ZUOLIN_URL((byte)1), THIRDPART_URL((byte)2), POST((byte)3),
    COUPON((byte)4);
    
    private byte code;
    
    private ActionType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ActionType fromCode(Byte code) {
        if(code == null)
            return null;
        
        ActionType[] values = ActionType.values();
        for(ActionType value : values) {
            if(value.code == code.byteValue()) {
                return value;
            }
        }
        
        return null;
    }
}
