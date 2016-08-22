package com.everhomes.rest.rentalv2;

import com.everhomes.rest.techpark.punch.PunchStatus;
/**
 * <ul>预定时间类型
 * <li>AM(0): 上午</li>
 * <li>PM(1): 下午</li>
 * <li>NIGHT(2): 晚上</li>
 * 
 * </ul>
 */
public enum AmorpmFlag {
   
	AM((byte)0),PM((byte)1),NIGHT((byte)2);
    
    private byte code;
    private AmorpmFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AmorpmFlag fromCode(byte code) {
        for(AmorpmFlag t : AmorpmFlag.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
