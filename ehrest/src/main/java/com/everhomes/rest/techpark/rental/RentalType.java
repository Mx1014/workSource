package com.everhomes.rest.techpark.rental;

import com.everhomes.rest.techpark.punch.PunchStatus;
/**
 * <ul>预定时间类型
 * <li>HOUR(0): 按小时</li>
 * <li>HALFDAY(1): 按半天</li>
 * <li>DAY(2): 按天</li>
 * 
 * </ul>
 */
public enum RentalType {
   
    HOUR((byte)0),DAY((byte)2),HALFDAY((byte)1),THREETIMEADAY((byte)3);
    
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
