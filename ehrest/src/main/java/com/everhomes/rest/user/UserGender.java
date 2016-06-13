// @formatter:off
package com.everhomes.rest.user;

public enum UserGender {
    UNDISCLOSURED((byte)0), MALE((byte)1), FEMALE((byte)2);
    
    private byte code;
    
    private UserGender(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static UserGender fromCode(Byte code) {
        if(code != null) {
            for(UserGender value : UserGender.values()) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
