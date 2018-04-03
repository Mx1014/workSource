// @formatter:off
package com.everhomes.rest.user;

/**
 * <ul>
 *     <li>NONE(0): 无</li>
 *     <li>MUTE(1): 免打扰</li>
 * </ul>
 */
public enum UserMuteNotificationFlag {
    NONE((byte)0), MUTE((byte)1);

    private byte code;

    UserMuteNotificationFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static UserMuteNotificationFlag fromCode(Byte code) {
        if (code != null) {
            for (UserMuteNotificationFlag flag : UserMuteNotificationFlag.values()) {
                if (flag.getCode() == code) {
                    return flag;
                }
            }
        }
        return null;
    }
}
