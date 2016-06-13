package com.everhomes.rest.techpark.punch;

/**
 * <ul>什么时间打卡
 * <li>AFTERNOONARRIVE(3): 下午上班</li>
 * <li>NOONLEAVE(2): 中午下班</li>
 * <li>LEAVE(1): 下班(晚上)</li>
 * <li>ARRIVE(0): 上班(早上)</li>
 * </ul>
 */
public enum ClockStatus {
    AFTERNOONARRIVE((byte)3),NOONLEAVE((byte)2), LEAVE((byte)1), ARRIVE((byte)0);
    
    private byte code;
    private ClockStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ClockStatus fromCode(byte code) {
        for(ClockStatus t : ClockStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
