// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>GENERAL((byte)1):一般</li>
 * <li>URGENT((byte)2):紧急</li>
 * <li>VERY_URGENT((byte)3):加急</li>
 * </ul>
 */
public enum PmTaskPriority {
	GENERAL((byte)1), URGENT((byte)2), VERY_URGENT((byte)3);
    
    private Byte code;
    private PmTaskPriority(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }
    
    public static PmTaskPriority fromCode(Byte code) {
        if(code != null) {
            PmTaskPriority[] values = PmTaskPriority.values();
            for(PmTaskPriority value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
