package com.everhomes.techpark.punch;

/**
 * <ul>什么时间打卡
 * <li>AFTERNOONARRIVE(3): 下午上班</li>
 * <li>NOONLEAVE(2): 中午下班</li>
 * <li>LEAVE(1): 下班(晚上)</li>
 * <li>ARRIVE(0): 上班(早上)</li>
 * </ul>
 */
public enum ClockStatus {
    AFTERNOONARRIVE(3),NOONLEAVE(2), LEAVE(1), ARRIVE(0);
    
    private int code;
    private ClockStatus(int code) {
        this.code = code;
    }
    
    public Integer getCode() {
        return this.code;
    }
    
    public static ClockStatus fromCode(int code) {
        for(ClockStatus t : ClockStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
