// @formatter:off
package com.everhomes.rest.family;

/**
 * <ul>参数类型
 * <li>FAMILY: 家庭</li>
 * <li>COMMUNITY: 小区</li>
 * </ul>
 */
public enum ParamType {
    FAMILY((byte)0), COMMUNITY((byte)1);
    
    private byte code;
    private ParamType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParamType fromCode(Byte code) {
        if(code == null)
            return FAMILY;
        
        switch(code.byteValue()) {
        case 0:
            return FAMILY;
            
        case 1:
            return COMMUNITY;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
