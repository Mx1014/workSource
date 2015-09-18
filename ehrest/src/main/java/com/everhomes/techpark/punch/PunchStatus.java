package com.everhomes.techpark.punch;

/**
 * <ul>打卡的状态
 * <li>UNPUNCH(3): 未打卡</li>
 * <li>LEAVEEARLY(2): 早退</li>
 * <li>BELATE(1): 迟到</li>
 * <li>NORMAL(0): 正常</li>
 * </ul>
 */
public enum PunchStatus {
    UNPUNCH(3),LEAVEEARLY(2), BELATE(1), NORMAL(0);
    
    private int code;
    private PunchStatus(int code) {
        this.code = code;
    }
    
    public Integer getCode() {
        return this.code;
    }
    
    public static PunchStatus fromCode(int code) {
        for(PunchStatus t : PunchStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
