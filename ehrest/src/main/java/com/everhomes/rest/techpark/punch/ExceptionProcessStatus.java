package com.everhomes.rest.techpark.punch;

/**
 * <ul>
 * <li>INACTIVE(0): 无效-拒绝</li>
 * <li>WAITFOR(1):待审核 </li>
 * <li>ACTIVE(2): 同意</li>
 * </ul>
 */
public enum ExceptionProcessStatus {
    INACTIVE((byte)0),WAITFOR((byte)1),ACTIVE((byte)2);
    
    private byte code;
    private ExceptionProcessStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ExceptionProcessStatus fromCode(byte code) {
        for(ExceptionProcessStatus t : ExceptionProcessStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
