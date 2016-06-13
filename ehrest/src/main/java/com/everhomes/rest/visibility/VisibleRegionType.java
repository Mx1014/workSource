// @formatter:off
package com.everhomes.rest.visibility;

/**
 * <p>区域范围：</p>
 * <ul>
 * <li>COMMUNITY: 园区（含小区）</li>
 * <li>REGION: 片区（机构）</li>
 * </ul>
 *
 */
public enum VisibleRegionType {
    COMMUNITY((byte)0), REGION((byte)1); 
    
    private byte code;
    
    private VisibleRegionType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static VisibleRegionType fromCode(Byte code) {
        VisibleRegionType[] values = VisibleRegionType.values();
        for(VisibleRegionType value : values) {
            if(Byte.valueOf(value.code).equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
