package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>0,"管理员"</li>
 * <li>1,"租户"</li>
 * <li>2,"负责人"</li>
 * <li>3,"工人"</li>
 * </ul>
 */
public enum ProcessorType {
    ROOT((byte)0,"管理员"),
    MASTER((byte)1,"租户"),
    CHIEF((byte)2,"负责人"),
    WORKER((byte)3,"工人");

    private byte code;
    private String describe;
    private ProcessorType(byte code,String describe) {
        this.code = code;
        this.describe = describe;
    }
    public byte getCode() {
        return this.code;
    }

    public static ProcessorType fromCode(byte code) {
        for(ProcessorType t : ProcessorType.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }

    public String getDescribe() {
        return describe;
    }
}
