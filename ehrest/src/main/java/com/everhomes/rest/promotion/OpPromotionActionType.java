package com.everhomes.rest.promotion;

/**
 * <ul>
 * <li>STATIC_WEB_PAGE: 静态网页</li>
 * <li>COUPON: 优惠券</li>
 * </ul>
 * @author janson
 *
 */
public enum OpPromotionActionType {
    STATIC_WEB_PAGE((byte)0), COUPON((byte)1), TEXT_ONLY((byte)2);
    
    private byte code;
    
    private OpPromotionActionType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OpPromotionActionType fromCode(Byte code) {
        if(code != null) {
            OpPromotionActionType[] values = OpPromotionActionType.values();
            for(OpPromotionActionType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
