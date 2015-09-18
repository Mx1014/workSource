package com.everhomes.techpark.punch;
/**
 * <ul>打卡制度
 * <li>STANDARD((byte)0): 标准的非弹性的</li>
 * <li>FLEX((byte)1): 弹性工作制</li> 
 * </ul>
 */
public enum PunchCheckType {
    STANDARD((byte)0),FLEX((byte)1);
    
    private byte code;
    private PunchCheckType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PunchCheckType fromCode(byte code) {
        for(PunchCheckType t : PunchCheckType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
