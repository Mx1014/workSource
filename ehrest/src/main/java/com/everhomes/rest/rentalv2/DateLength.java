package com.everhomes.rest.rentalv2;

import com.everhomes.rest.techpark.punch.PunchStatus;
/**
 * <ul>时间长度
 * <li>DAY(0): 日</li>
 * <li>WEEK(1): 周</li>
 * <li>MONTH(2): 月</li>
 * 
 * </ul>
 */
public enum DateLength {
   
    DAY((byte)0),WEEK((byte)1),MONTH((byte)2);
    
    private byte code;
    private DateLength(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static DateLength fromCode(byte code) {
        for(DateLength t : DateLength.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
