package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>CHECK_IN((byte) 0): 入职</li>
 * <li>EMPLOY((byte) 1): 转正</li>
 * <li>TRANSFER((byte) 2): 调动</li>
 * <li>DISMISS((byte)3): 离职</li>
 * </ul>
 */
public enum ArchivesOperationType {

    CHECK_IN((byte) 0), EMPLOY((byte) 1), TRANSFER((byte) 2), DISMISS((byte) 3);
    private Byte code;

    private ArchivesOperationType(byte code) {
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
