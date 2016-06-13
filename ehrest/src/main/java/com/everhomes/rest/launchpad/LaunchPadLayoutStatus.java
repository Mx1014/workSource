// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>服务市场风格状态
 * <li>INACTIVE(0): 无效的</li>
 * <li>WAITING_FOR_CONFIRMATION(1): 待确认</li>
 * <li>ACTIVE(2): 正常</li>
 * </ul>
 */
public enum LaunchPadLayoutStatus {
    INACTIVE((byte)0), WAITING_FOR_CONFIRMATION((byte)1),ACTIVE((byte)2);
    
    private byte code;
    private LaunchPadLayoutStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static LaunchPadLayoutStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return INACTIVE;
            
        case 1:
            return WAITING_FOR_CONFIRMATION;
            
        case 2:
            return ACTIVE;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
