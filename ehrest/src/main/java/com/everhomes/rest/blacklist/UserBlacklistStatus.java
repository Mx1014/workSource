package com.everhomes.rest.blacklist;


public enum UserBlacklistStatus {
	INACTIVE((byte)0), CONFIRMING((byte)1), ACTIVE((byte)2);

    private byte code;
    private UserBlacklistStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static UserBlacklistStatus fromCode(Byte code) {
        if(code != null) {
            for(UserBlacklistStatus value : UserBlacklistStatus.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}