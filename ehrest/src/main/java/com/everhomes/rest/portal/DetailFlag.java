// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>NO: 不要详情</li>
 * <li>YES: 要详情</li>
 * </ul>
 */
public enum DetailFlag {
    NO((byte)0), YES((byte)1);

    private byte code;

    private DetailFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static DetailFlag fromCode(Byte code) {
        if(null != code){
            for (DetailFlag value: DetailFlag.values()) {
                if(value.code == code.byteValue()){
                    return value;
                }
            }
        }
        return null;
    }
}
