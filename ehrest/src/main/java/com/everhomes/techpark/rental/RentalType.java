package com.everhomes.techpark.rental;

import com.everhomes.techpark.punch.PunchStatus;
/**
 * <ul>预定时间类型
 * <li>HOUR(0): 按小时</li>
 * <li>DAY(1): 按天</li>
 * </ul>
 */
public enum RentalType {
   
    HOUR((byte)0),DAY((byte)1);
    
    private byte code;
    private RentalType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RentalType fromCode(byte code) {
        for(RentalType t : RentalType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
