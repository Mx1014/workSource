package com.everhomes.rest.archives;

/**
 * <ul>
 * <li>CHECK_IN((byte) 0): 入职</li>
 * <li>EMPLOY((byte) 1): 转正</li>
 * <li>TRANSFER((byte) 2): 调动</li>
 * <li>DISMISS((byte)3): 离职</li>
 * <li>SELF_EMPLOY((byte)4): 申请转正</li>
 * <li>SELF_DISMISS((byte)6): 申请离职</li>
 * </ul>
 */
public enum ArchivesOperationType {

    CHECK_IN((byte) 0), EMPLOY((byte) 1), TRANSFER((byte) 2), DISMISS((byte) 3),
    SELF_EMPLOY((byte) 4), SELF_DISMISS((byte) 6);
    private Byte code;

    ArchivesOperationType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ArchivesOperationType fromCode(Byte code) {
        if(code != null) {
            ArchivesOperationType[] values = ArchivesOperationType.values();
            for(ArchivesOperationType value : values) {
                if(code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}
