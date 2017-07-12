// @formatter:off
package com.everhomes.rest.oauth2client;

/**
 * <ul>
 *     <li>ACCESS_TOKEN(0): access token</li>
 *     <li>REFRESH_TOKEN(1): refresh token</li>
 * </ul>
 */
public enum OAuth2ClientTokenType {

    ACCESS_TOKEN((byte)0), REFRESH_TOKEN((byte)1);

    private Byte code;

    OAuth2ClientTokenType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static OAuth2ClientTokenType fromCode(Byte code) {
        if (code != null) {
            for (OAuth2ClientTokenType type : OAuth2ClientTokenType.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
