// @formatter:off
package com.everhomes.rest.family;

/**
 * <ul>参数类型
 * <li>UNKNOWN: 0-未知</li>
 * <li>SAMEFLOOR: 1-同层</li>
 * <li>SAMEBUILDING: 2-同楼</li>
 * </ul>
 */
public enum NeighborhoodRelation {
    UNKNOWN((byte)0), SAMEFLOOR((byte)1), SAMEBUILDING((byte)2);
    
    private byte code;
    private NeighborhoodRelation(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static NeighborhoodRelation fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return UNKNOWN;
            
        case 1:
            return SAMEFLOOR;
            
        case 2:
            return SAMEBUILDING;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
