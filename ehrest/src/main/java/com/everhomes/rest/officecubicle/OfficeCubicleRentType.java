// @formatter:off
package com.everhomes.rest.officecubicle;

/**
 * <ul>
 * <li>SHORT_RENT((byte)0): 短租</li>
 * <li>LONG_RENT((byte)1): 长租</li>
 * </ul>
 */
public enum OfficeCubicleRentType {
	SHORT_RENT((byte)0,"短租"),LONG_RENT((byte)1,"长租");

    private byte code;
	private String desc;

    private OfficeCubicleRentType(byte code, String desc) {
        this.code = code;
		this.desc = desc;
    }
    
	public String getDesc(){
		return desc;
	}
	
    public byte getCode() {
        return this.code;
    }
    
    public static OfficeCubicleRentType fromCode(Byte code) {
        if(code != null) {
        	OfficeCubicleRentType[] values = OfficeCubicleRentType.values();
            for(OfficeCubicleRentType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
