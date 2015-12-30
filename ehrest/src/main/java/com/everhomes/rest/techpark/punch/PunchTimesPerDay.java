package com.everhomes.rest.techpark.punch;
/**
 * <ul>打几次卡一天
 * <li>TWICE(2): 2次</li>
 * <li>FORTH(4): 4次</li>
 * </ul>
 */
public enum PunchTimesPerDay {
    TWICE((byte)2),FORTH((byte)4);
    
    private Byte code;
    private PunchTimesPerDay(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }
    
    public static PunchTimesPerDay fromCode(Byte code) {
        for(PunchTimesPerDay t : PunchTimesPerDay.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
