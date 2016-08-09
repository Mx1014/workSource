// @formatter:off
package com.everhomes.rest.wanke;

/**
 * <ul>参数类型
 * <li>NONE(0): 无</li>
 * </ul>
 */
public enum ActionType {
      NONE((byte)0), URL((byte)1);
    private byte code;
    
    private ActionType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ActionType fromCode(Byte code) {
        if(code == null)
            return null;
        
        ActionType[] values = ActionType.values();
        for(ActionType value : values) {
            if(value.code == code.byteValue()) {
                return value;
            }
        }
        
        return null;
    }
}
