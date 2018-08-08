// @formatter:off
package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>DURATION((byte)0): 时长</li>
 * <li>COUNT((byte)1):1, 次数</li>
 * </ul>
 *
 */
public enum DoorAuthRuleType {
	DURATION((byte)0), COUNT((byte)1);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private DoorAuthRuleType(byte code) {
        this.code = code;
    }
    
    public static DoorAuthRuleType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return DURATION;
            
        case 1 :
            return COUNT;
        }
        
        return null;
    }
}
