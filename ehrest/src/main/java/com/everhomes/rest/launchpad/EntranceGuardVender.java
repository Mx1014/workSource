// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>门禁厂商
 * <li>NONE(0)：默认</li>
 * <li>XINLIAN(1): 新联</li>
 * </ul>
 */
public enum EntranceGuardVender {
    NONE((byte)0),XINLIAN((byte)1);
    
    private byte code;
       
       
    private EntranceGuardVender(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static EntranceGuardVender fromCode(byte code) {
           EntranceGuardVender[] values = EntranceGuardVender.values();
           for(EntranceGuardVender value : values) {
               if(value.code == code) {
                   return value;
               }
           }
           
           return null;
       }
}
