package com.everhomes.techpark.punch;

/**
 * <ul>异常申报类型
 * <li>FORGOT(0): 漏打卡</li>
 * <li>BIZOUT(1): 公出</li>
 * <li>SICK(2): 病假</li>
 * <li>ABSENCE(3): 事假</li>
 * <li>EXCHANGE(4): 调休</li>
 * <li>OTHER(5): 其他</li>
 * </ul>
 */
public enum PunchRquestType {
	OTHER((byte)5),EXCHANGE((byte)4),ABSENCE((byte)3),SICK((byte)2), BIZOUT((byte)1), FORGOT((byte)0);
    
    private byte code;
    private PunchRquestType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PunchRquestType fromCode(byte code) {
        for(PunchRquestType t : PunchRquestType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
