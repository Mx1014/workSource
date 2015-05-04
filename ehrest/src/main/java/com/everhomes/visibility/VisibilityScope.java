// @formatter:off
package com.everhomes.visibility;

/**
 * <p>可见性范围：</p>
 * <ul>
 * <li>ALL: 所有人可见</li>
 * <li>COMMUNITY_ONLY: 仅本小区可见</li>
 * <li>NEARBY_COMMUNITIES: 小区周边可见</li>
 * <li>CITY_ONLY: 同城可见</li>
 * <li>EXPLICIT_CONFIGURED: 指定区域可见</li>
 * </ul>
 *
 */
public enum VisibilityScope {
    ALL((byte)0), COMMUNITY_ONLY((byte)1), NEARBY_COMMUNITIES((byte)2), 
    CITY_ONLY((byte)3), EXPLICIT_CONFIGURED((byte)4); 
    
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
            return COMMUNITY_ONLY;
            
        case 2 :
            return NEARBY_COMMUNITIES;
            
        case 3 :
            return CITY_ONLY;
            
        case 4 :
            return EXPLICIT_CONFIGURED;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
