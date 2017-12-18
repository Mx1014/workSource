// @formatter:off
package com.everhomes.rest.reserve;

/**
 * <ul>
 * <li>NOT_SUPPORT(0): 不支持</li>
 * <li>SUPPORT(1): 支持</li>
 * </ul>
 */
public enum ReserveCommonFlag {
	NOT_SUPPORT((byte)0), SUPPORT((byte)1);

    private byte code;

    private ReserveCommonFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ReserveCommonFlag fromCode(Byte code) {
        if(code != null) {
            ReserveCommonFlag[] values = ReserveCommonFlag.values();
            for(ReserveCommonFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
