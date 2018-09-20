// @formatter:off
package com.everhomes.rest.workReport;

/**
 * <ul>
 * <li>NO(0): 否</li>
 * <li>YES(1): 是</li>
 * </ul>
 */
public enum AttitudeFlag {

    NO((byte)0), YES((byte)1);

    private byte code;

    private AttitudeFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static AttitudeFlag fromCode(Byte code) {
        if(code == null)
            return null;
        AttitudeFlag[] values = AttitudeFlag.values();
        for(AttitudeFlag value : values){
            if(value.getCode() == code)
                return value;
        }
        
        return null;
    }
}
