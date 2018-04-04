package com.everhomes.rest.pmtask;

/**
 * 报修来源
 * SELF 用户发起
 * OTHERS 员工代发
 */
public enum PmtaskCreatorType {
    SELF((byte)1), OTHERS((byte)2);

    private byte code;
    PmtaskCreatorType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PmtaskCreatorType fromCode(Byte code) {
        if(code != null) {
            PmtaskCreatorType[] values = PmtaskCreatorType.values();
            for(PmtaskCreatorType value : values) {
                if(value.code == code) {
                    return value;
                }
            }
        }

        return null;
    }
}
