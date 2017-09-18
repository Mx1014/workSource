// @formatter:off
package com.everhomes.rest.user;

/**
 * <ul>
 *  <li>APP: 来源app用户</li>
 *  <li>WEIXIN: 来源微信</li>
 * </ul>
 */
public enum UserSourceType {
    APP((byte)1), WEIXIN((byte)2);

    private byte code;
    private UserSourceType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
   
    public static UserSourceType fromCode(Byte code) {
        if(code != null) {
            UserSourceType[] values = UserSourceType.values();
            for(UserSourceType value : values) {
                if(value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
