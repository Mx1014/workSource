// @formatter:off
package com.everhomes.rest.visibility;

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
        if(code != null) {
            VisibilityScope[] values = VisibilityScope.values();
            for(VisibilityScope value : values) {
                if(value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
