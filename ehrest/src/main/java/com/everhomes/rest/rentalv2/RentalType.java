package com.everhomes.rest.rentalv2;

/**
 * <ul>预定时间类型
 * <li>HOUR(0): 按小时</li>
 * <li>HALFDAY(1): 按半天</li>
 * <li>DAY(2): 按天</li>
 * <li>THREETIMEADAY(3): 按半天 (支持晚上)</li>
 * <li>MONTH(4): 按月</li>
 * </ul>
 */
public enum RentalType {
   
    HOUR((byte)0),DAY((byte)2),HALFDAY((byte)1),THREETIMEADAY((byte)3), MONTH((byte)4);
    
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
