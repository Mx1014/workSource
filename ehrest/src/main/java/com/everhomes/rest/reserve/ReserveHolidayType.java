// @formatter:off
package com.everhomes.rest.reserve;

/**
 * <ul>
 * <li>NORMAL_WEEKEND(1): 普通双休</li>
 * <li>AREA_HOLIDAY(2): 地区节假日</li>
 * </ul>
 */
public enum ReserveHolidayType {
	NORMAL_WEEKEND((byte)1), AREA_HOLIDAY((byte)2);

    private byte code;

    private ReserveHolidayType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ReserveHolidayType fromCode(Byte code) {
        if(code != null) {
            ReserveHolidayType[] values = ReserveHolidayType.values();
            for(ReserveHolidayType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
