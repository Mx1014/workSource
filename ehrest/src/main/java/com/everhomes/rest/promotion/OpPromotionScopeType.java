// @formatter:off
package com.everhomes.rest.promotion;

/**
 * <ul>运营推广活动范围
 * <li>ALL(0): 所有</li>
 * <li>COMMUNITY(1): 整体小区</li>
 * <li>CITY(2): 整个城市</li>
 * <li>USER(3): 某个用户</li>
 * <li>ORGANIZATION(4): 某个公司/机构</li>
 * </ul>
 */
public enum OpPromotionScopeType {
    ALL((byte)0), COMMUNITY((byte)1), CITY((byte)2), USER((byte)3), ORGANIZATION((byte)4);
    
    private byte code;
    
    private OpPromotionScopeType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OpPromotionScopeType fromCode(Byte code) {
        if(code != null) {
            OpPromotionScopeType[] values = OpPromotionScopeType.values();
            for(OpPromotionScopeType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
