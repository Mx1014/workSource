package com.everhomes.techpark.punch;
/**
 * <ul>打几次卡一天
 * <li>TWICE(2): 2次</li>
 * <li>FORTH(4): 4次</li>
 * </ul>
 */
public enum PunchTimesPerDay {
    TWICE(2),FORTH(4);
    
    private Integer code;
    private PunchTimesPerDay(Integer code) {
        this.code = code;
    }
    
    public Integer getCode() {
        return this.code;
    }
    
    public static PunchTimesPerDay fromCode(Integer code) {
        for(PunchTimesPerDay t : PunchTimesPerDay.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
