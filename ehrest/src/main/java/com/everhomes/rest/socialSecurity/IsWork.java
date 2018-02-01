package com.everhomes.rest.socialSecurity;

/**
 * <ul>
 * <li> IS_NEW((byte)1),IS_OUT((byte)-1),NORMAL((byte)0)</li>
 * </ul>
 */
public enum IsWork {

	IS_NEW((byte)1),IS_OUT((byte)-1),NORMAL((byte)0);

    private byte code;
    private IsWork(Byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static IsWork fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for(IsWork t : IsWork.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
