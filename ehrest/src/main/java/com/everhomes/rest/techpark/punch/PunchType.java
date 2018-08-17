package com.everhomes.rest.techpark.punch;

/**
 * <ul>0:weekend work date ; 1:holiday
 * <li>ON_DUTY(0): 上班打卡</li>
 * <li>OFF_DUTY(1): 下班打卡</li>
 * <li>NOT_WORKTIME(2): 没到上班时间</li>
 * <li>NOT_WORKDAY(3): 今天不上班</li>
 * <li>MEIPAIBAN(4): 今天没排班</li>
 * <li>FINISH(5): 今天打完了</li>
 * <li>OVERTIME_ON_DUTY(6): 加班签到</li>
 * <li>OVERTIME_OFF_DUTY(7): 加班签退</li>
 * </ul>
 */
public enum PunchType {
    ON_DUTY((byte) 0), OFF_DUTY((byte) 1), NOT_WORKTIME((byte) 2), NOT_WORKDAY((byte) 3), MEIPAIBAN((byte) 4), FINISH((byte) 5), OVERTIME_ON_DUTY((byte) 6), OVERTIME_OFF_DUTY((byte) 7);
    
    private byte code;
    private PunchType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PunchType fromCode(Byte code) {
        for(PunchType t : PunchType.values()) {
        	if(null !=code){
	            if (code.equals(t.code)) {
	                return t;
	            }
            }
        }
        
        return null;
    }


    @Override
    public String toString() {
        switch (this.code) {
            case 0:
                return "上班";
            case 1:
                return "下班";
            case 6:
                return "加班签到";
            case 7:
                return "加班签退";
            default:
                return "";
        }
    }
}
