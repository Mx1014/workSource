package com.everhomes.techpark.punch;
/**
 * <ul>打几次卡一天
 * <li>TWICE(2): 2次</li>
 * <li>FORTH(4): 4次</li>
 * </ul>
 */
public enum punchTimes {
    TWICE(2),FORTH(4);
    
    private int code;
    private punchTimes(int code) {
        this.code = code;
    }
    
    public Integer getCode() {
        return this.code;
    }
    
    public static punchTimes fromCode(int code) {
        for(punchTimes t : punchTimes.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
