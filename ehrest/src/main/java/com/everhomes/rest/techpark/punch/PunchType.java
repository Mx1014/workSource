package com.everhomes.rest.techpark.punch;

/**
 * <ul>0:weekend work date ; 1:holiday
 * <li>ON_DUTY(0): 上班打卡</li>
 * <li>OFF_DUTY(1): 下班打卡</li>
 * <li>NOT_WORKTIME(2): 非打卡时间不能打卡</li>
 * </ul>
 */
public enum PunchType {
    ON_DUTY((byte)0),OFF_DUTY((byte)1),NOT_WORKTIME((byte)2);
    
    private byte code;
    private PunchType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PunchType fromCode(byte code) {
        for(PunchType t : PunchType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
