package com.everhomes.techpark.punch;
/**
 * <ul>打几次卡一天
 * <li>TWICE(2): 2次</li>
 * <li>FORTH(4): 4次</li>
 * </ul>
 */
public enum PunchTimes {
    TWICE(2),FORTH(4);
    
    private Integer code;
    private PunchTimes(Integer code) {
        this.code = code;
    }
    
    public Integer getCode() {
        return this.code;
    }
    
    public static PunchTimes fromCode(Integer code) {
        for(PunchTimes t : PunchTimes.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
