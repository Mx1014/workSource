// @formatter:off
package com.everhomes.rest.techpark.expansion;

/**
 * <ul>园区入驻参数状态
 * <li>DISABLED(0): 禁用</li>
 * <li>ENABLED(1): 启用</li>
 * </ul>
 */
public enum LeasePromotionFlag {
    DISABLED((byte)0), ENABLED((byte)1);

    private byte code;

    private LeasePromotionFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static LeasePromotionFlag fromCode(Byte code) {
        if(code != null) {
            LeasePromotionFlag[] values = LeasePromotionFlag.values();
            for(LeasePromotionFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
