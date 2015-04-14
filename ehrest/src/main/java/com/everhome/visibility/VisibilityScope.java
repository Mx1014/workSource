// @formatter:off
package com.everhome.visibility;

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
