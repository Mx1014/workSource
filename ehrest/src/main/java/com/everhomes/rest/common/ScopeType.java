// @formatter:off
package com.everhomes.rest.common;

/**
 * <ul>
 * <li>ALL(0): 国家</li>
 * <li>COMMUNITY(1): 园区</li>
 * <li>CITY(2): 城市</li>
 * <li>USER(3): 用户</li>
 * <li>ORGANIZATION(4): 机构/公司</li>
 * <li>PM(5): 物业公司</li>
 * <li>RESIDENTIAL(6): 小区</li>
 * </ul>
 */
public enum ScopeType {
    ALL((byte)0), COMMUNITY((byte)1), CITY((byte)2),USER((byte)3),ORGANIZATION((byte)4),PM((byte)5), RESIDENTIAL((byte)6);
    
    private byte code;
    
    private ScopeType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static ScopeType fromCode(Byte code) {
        if(code == null)
            return null;
        ScopeType[] values = ScopeType.values();
        for(ScopeType value : values){
            if(value.getCode() == code.byteValue())
                return value;
        }
        
        return null;
    }
}
