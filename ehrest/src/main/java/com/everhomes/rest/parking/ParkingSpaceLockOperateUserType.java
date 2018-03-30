// @formatter:off
package com.everhomes.rest.parking;

/**
 * <p>vip车位 操作用户类型</p>
 * <ul>
 * <li>RESERVE_PERSON(1): 预约人</li>
 * <li>PLATE_OWNER(2): 车主</li>
 * </ul>
 */
public enum ParkingSpaceLockOperateUserType {
    RESERVE_PERSON((byte)1,"预约人"), PLATE_OWNER((byte)2,"车主");

    private byte code;
    private String desc;
    private ParkingSpaceLockOperateUserType(byte code,String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public String getDesc() {
		return desc;
	}

	public static ParkingSpaceLockOperateUserType fromCode(Byte code) {
        if(code != null) {
            ParkingSpaceLockOperateUserType[] values = ParkingSpaceLockOperateUserType.values();
            for(ParkingSpaceLockOperateUserType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
