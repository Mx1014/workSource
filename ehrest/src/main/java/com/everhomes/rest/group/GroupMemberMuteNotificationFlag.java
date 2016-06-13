// @formatter:off
package com.everhomes.rest.group;

/**
 * <ul>group成员的是否免打扰
 * <li>NONE: 无</li>
 * <li>MUTE: 免打扰</li>
 * </ul>
 */
public enum GroupMemberMuteNotificationFlag {
    NONE((byte)0), MUTE((byte)1);
    
    private byte code;
    
    private GroupMemberMuteNotificationFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static GroupMemberMuteNotificationFlag fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return NONE;
            
        case 1 :
            return MUTE;
            
        default :
            break;
        }
        
        return null;
    }
}
