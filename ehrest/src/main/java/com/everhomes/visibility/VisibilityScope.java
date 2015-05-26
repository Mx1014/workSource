// @formatter:off
package com.everhomes.visibility;

/**
 * <p>可见性范围：</p>
 * <ul>
 * <li>ALL: 所有人可见</li>
 * <li>COMMUNITY: 仅本小区可见</li>
 * <li>NEARBY_COMMUNITIES: 小区周边可见</li>
 * <li>CITY: 同城可见</li>
 * </ul>
 *
 */
public enum VisibilityScope {
    ALL((byte)0), COMMUNITY((byte)1), NEARBY_COMMUNITIES((byte)2), CITY((byte)3); 
    
    private byte code;
    
    private VisibilityScope(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static VisibilityScope fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return ALL;
            
        case 1 :
            return COMMUNITY;
            
        case 2 :
            return NEARBY_COMMUNITIES;
            
        case 3 :
            return CITY;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
