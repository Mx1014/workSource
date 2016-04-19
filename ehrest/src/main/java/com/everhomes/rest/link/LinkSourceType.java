// @formatter:off
package com.everhomes.rest.link;

/**
 * <ul>
 * <li>NONE(0): 无来源</li>
 * <li>POST(1): 来源于帖子</li>
 * </ul>
 */
public enum LinkSourceType {
    NONE((byte)0), POST((byte)1);
    
    private byte code;
    private LinkSourceType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static LinkSourceType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return NONE;
            
        case 1:
            return POST;
       
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
