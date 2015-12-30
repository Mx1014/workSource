package com.everhomes.rest.techpark.punch;

/**
 * <ul>申报或者审批类型
 * <li>REQUEST(0): 异常申报</li>
 * <li>APPROVAL(1): 异常审批</li>
 * </ul>
 */
public enum PunchRquestType {
	APPROVAL((byte)1), REQUEST((byte)0);
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
