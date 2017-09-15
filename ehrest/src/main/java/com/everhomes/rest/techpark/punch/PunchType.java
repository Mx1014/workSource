package com.everhomes.rest.techpark.punch;

/**
 * <ul>0:weekend work date ; 1:holiday
 * <li>ON_DUTY(0): 上班打卡</li>
 * <li>OFF_DUTY(1): 下班打卡</li>
 * <li>NOT_WORKTIME(2): 没到上班时间</li>
 * <li>NOT_WORKDAY(3): 今天不上班</li>
 * <li>MEIPAIBAN(4): 今天没排班</li>
 * <li>FINISH(5): 今天打完了</li>
 * </ul>
 */
public enum PunchType {
    ON_DUTY((byte)0),OFF_DUTY((byte)1),NOT_WORKTIME((byte)2),NOT_WORKDAY((byte)3),MEIPAIBAN((byte)4),FINISH((byte)5);
    
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
