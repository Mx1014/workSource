package com.everhomes.rest.socialSecurity;

/**
 * <ul>
 * <li>SOCAIL(1):社保 </li>
 * <li>ACCUM(2):公积金 </li>
 * </ul>
 */
public enum AccumOrSocial {

	SOCAIL((byte)1),ACCUM((byte)2);

    private byte code;
    private AccumOrSocial(Byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AccumOrSocial fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for(AccumOrSocial t : AccumOrSocial.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
