// @formatter:off
package com.everhomes.rest.user;

public enum UserGender {
    UNDISCLOSURED((byte)0, "未知"), MALE((byte)1, "男"), FEMALE((byte)2, "女");
    
    private byte code;
    private String text;
    
    private UserGender(byte code, String text) {
        this.code = code;
        this.text = text;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public String getText() {
    	return text;
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

    public static UserGender fromText(String text) {
        if(text != null) {
            for(UserGender value : UserGender.values()) {
                if (value.text.equals(text)) {
                    return value;
                }
            }
        }

        return null;
    }
}
