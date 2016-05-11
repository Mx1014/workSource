package com.everhomes.rest.promotion;

public enum OpPromotionConditionType {
    ALL((byte)0), NEW_USER((byte)1), ORDER_RANGE_VALUE((byte)2);
    
    private byte code;
    
    private OpPromotionConditionType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OpPromotionConditionType fromCode(Byte code) {
        if(code != null) {
            OpPromotionConditionType[] values = OpPromotionConditionType.values();
            for(OpPromotionConditionType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
