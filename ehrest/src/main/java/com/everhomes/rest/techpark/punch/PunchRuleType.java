package com.everhomes.rest.techpark.punch;

/**
 * <ul>
 * <li>PAIBAN(0): 排班 </li>
 * <li>GUDING(1): 固定</li>
 * </ul>
 */
public enum PunchRuleType {
   
	GUDING((byte)1),PAIBAN((byte)0);
    
    private byte code;
    private PunchRuleType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static PunchRuleType fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        for (PunchRuleType t : PunchRuleType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        return null;
    }
}
