package com.everhomes.rest.quality;

/**
 * <p>范围：</p>
 * <ul>
 * <li>ALL: 全部</li>
 * <li>COMMUNITY: 小区</li>
 * </ul>
 *
 */
public enum SpecificationScopeCode {

	ALL((byte)0), COMMUNITY((byte)1); 
    
    private byte code;
    
    private SpecificationScopeCode(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static SpecificationScopeCode fromCode(Byte code) {
        if(code != null) {
        	SpecificationScopeCode[] values = SpecificationScopeCode.values();
            for(SpecificationScopeCode value : values) {
                if(value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
