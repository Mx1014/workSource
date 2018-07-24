package com.everhomes.rest.remind;

/**
 * <ul>
 * <li>UNDO((byte) 1): 未完成</li>
 * <li>DONE((byte) 2): 已完成</li>
 * </ul>
 */
public enum RemindStatus {
    UNDO((byte) 1), DONE((byte) 2);

    private byte code;

    RemindStatus(Byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static RemindStatus fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        RemindStatus[] values = RemindStatus.values();
        for (RemindStatus value : values) {
            if (value.code == code.byteValue()) {
                return value;
            }
        }
        return null;
    }
}
