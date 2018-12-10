// @formatter:off
package com.everhomes.rest.officecubicle;

/**
 * <ul>工位类型 
 * <li>LONG_RENT(1): 长租工位</li>
 * <li>SHOT_RENT(0): 短租工位</li> 
 * </ul>
 */
public enum OfficeCubiceRentType {
    LONG_RENT((byte)1, "长租工位"), SHOT_RENT((byte)0, "短租工位");
    
    private byte code;
    private String description;
    
    private OfficeCubiceRentType(byte code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OfficeCubiceRentType fromCode(Byte code) {
        if(code != null) {
        	OfficeCubiceRentType[] values = OfficeCubiceRentType.values();
            for(OfficeCubiceRentType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }

    public String getDescription() {
        return description;
    }
}
