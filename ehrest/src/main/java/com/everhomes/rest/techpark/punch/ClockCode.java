package com.everhomes.rest.techpark.punch;

/**
 * <ul>
 * <li>FAIL(0): 不在打卡范围内</li>
 * <li>SUCESS(1): 正常打卡成功</li>
 * </ul>
 */
public enum ClockCode {
    FAIL((byte)0),SUCESS((byte)1);
    
    private byte code;
    private ClockCode(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ClockCode fromCode(Byte code) {
    	if(null == code)
    		return null;
        for(ClockCode t : ClockCode.values()) {
            if (t.code == code.byteValue()) {
                return t;
            }
        }
        return null;
    }
}
