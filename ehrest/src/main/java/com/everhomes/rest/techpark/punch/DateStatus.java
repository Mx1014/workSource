package com.everhomes.rest.techpark.punch;

/**
 * <ul>0:weekend work date ; 1:holiday
 * <li>WEEKENDWORK(0): 周末要工作</li>
 * <li>HOLIDAY(1): 工作日的节假日</li>
 * </ul>
 */
public enum DateStatus {
    WEEKENDWORK((byte)0),HOLIDAY((byte)1);
    
    private byte code;
    private DateStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static DateStatus fromCode(byte code) {
        for(DateStatus t : DateStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
