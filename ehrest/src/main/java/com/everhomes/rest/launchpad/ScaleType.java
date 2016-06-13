// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>图标是否需要裁剪
 * <li>NONE(0)：不需要</li>
 * <li>TAILOR(1): 需要</li>
 * </ul>
 */
public enum ScaleType {
    NONE((byte)0),TAILOR((byte)1);
    
    private byte code;
       
       
    private ScaleType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ScaleType fromCode(byte code) {
           ScaleType[] values = ScaleType.values();
           for(ScaleType value : values) {
               if(value.code == code) {
                   return value;
               }
           }
           
           return null;
       }
}
