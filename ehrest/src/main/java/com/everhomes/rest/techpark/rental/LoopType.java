package com.everhomes.rest.techpark.rental;

import com.everhomes.rest.techpark.punch.PunchStatus;

/**
 * <ul>预定时间类型
 * <li>EVERYDAY(0): 每天</li>
 * <li>EVERYWEEK(1): 每周</li>
 * <li>EVERYMONTH(2): 每月</li>
 * <li>EVERYYEAR(3): 每年</li>
 * <li>ONLYTHEDAY(4): 就这一天</li>
 * </ul>
 */

public enum LoopType {
   EVERYDAY((byte)0),EVERYWEEK((byte)1),EVERYMONTH((byte)2),EVERYYEAR((byte)3),ONLYTHEDAY((byte)4);
   
    private byte code;
    private LoopType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static LoopType fromCode(byte code) {
        for(LoopType t : LoopType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
