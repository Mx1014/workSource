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
    UNPUNCH((byte)3),LEAVEEARLY((byte)2), BELATE((byte)1), NORMAL((byte)0);
    
    private byte code;
    private PunchStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PunchStatus fromCode(byte code) {
        for(PunchStatus t : PunchStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
