package com.everhomes.rest.enterprisepaymentauth;

/**
 * <ul>
 * <li>ADD((byte)1),CHANGE((byte)0), DELETE((byte) -1)</li>
 * </ul>
 */
public enum OperateType {

	ADD((byte)1),CHANGE((byte)0), DELETE((byte) -1);

    private byte code;
    private OperateType(Byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OperateType fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for(OperateType t : OperateType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
