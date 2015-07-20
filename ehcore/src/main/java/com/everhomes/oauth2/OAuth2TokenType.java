package com.everhomes.oauth2;

public enum OAuth2TokenType {
    ACCESS_TOKEN((byte)0), REFRESH_TOKEN((byte)1);

    private byte code;
    private OAuth2TokenType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static OAuth2TokenType fromCode(Byte code) {
        if(code == null)
            return null;

        switch(code.byteValue()) {
        case 0 :
            return ACCESS_TOKEN;

        case 1 :
            return REFRESH_TOKEN;

        default :
            break;
        }

        return null;
    }
}
