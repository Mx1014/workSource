package com.everhomes.rest.enterprisepaymentauth;

/**
 * <ul>
 * <li>NO(0): </li>
 * </ul>
 */
public enum MessageUrlType {
   
	PAYMENT_AUTH_LIMIT((byte)0);
    
    private byte code;
    private MessageUrlType(Byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static MessageUrlType fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for(MessageUrlType t : MessageUrlType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
