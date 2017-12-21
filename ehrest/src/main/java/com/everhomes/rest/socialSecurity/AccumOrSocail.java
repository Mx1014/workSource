package com.everhomes.rest.socialSecurity;

/**
 * <ul>
 * <li>SOCAIL(1):社保 </li>
 * <li>ACCUM(2):公积金 </li>
 * </ul>
 */
public enum AccumOrSocail {

	SOCAIL((byte)1),ACCUM((byte)2);

    private byte code;
    private AccumOrSocail(Byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AccumOrSocail fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for(AccumOrSocail t : AccumOrSocail.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
