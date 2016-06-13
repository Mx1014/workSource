// @formatter:off
package com.everhomes.rest.group;

/**
 * <ul>group成员的手机号是否公开标记
 * <li>PUBLIC: 公开</li>
 * <li>PRIVATE: 不公开</li>
 * </ul>
 */
public enum GroupMemberPhonePrivacy {

    PUBLIC((byte)0), PRIVATE((byte)1);
    
    private byte code;
    
    private GroupMemberPhonePrivacy(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static GroupMemberPhonePrivacy fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return PUBLIC;
            
        case 1 :
            return PRIVATE;
            
        default :
            break;
        }
        
        return null;
    }
}
