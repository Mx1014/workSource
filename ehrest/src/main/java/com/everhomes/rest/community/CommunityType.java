// @formatter:off
package com.everhomes.rest.community;

/**
 * <ul>园区类型
 * <li>RESIDENTIAL(0): 住宅类型小区</li>
 * <li>COMMERCIAL(1): 商用类型园区</li>
 * </ul>
 */
public enum CommunityType {
    RESIDENTIAL((byte)0), COMMERCIAL((byte)1);
    
    private byte code;
    
    private CommunityType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static CommunityType fromCode(Byte code) {
        if(code != null) {
            CommunityType[] values = CommunityType.values();
            for(CommunityType value : values) {
                if(code.byteValue() == value.getCode()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
