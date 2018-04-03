package com.everhomes.rest.techpark.punch;

import com.everhomes.rest.techpark.punch.PunchStatus;
/**
 * <ul>
 * <li>0:normal普通 </li>
 * <li>1:NONENTRY未入职</li>
 * <li>2:RESIGNED已离职 </li>
 * </ul>
 */
public enum PunchUserStatus {
   
	NORMAL((byte)0),NONENTRY((byte)1),RESIGNED((byte)2);
    
    private byte code;
    private PunchUserStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PunchUserStatus fromCode(byte code) {
        for(PunchUserStatus t : PunchUserStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
