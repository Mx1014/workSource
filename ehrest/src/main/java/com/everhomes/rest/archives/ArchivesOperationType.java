package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>EMPLOY((byte) 0): 转正</li>
 * <li>TRANSFER((byte) 1): 调整</li>
 * <li>DISMISS((byte) 2): 离职</li>
 * </ul>
 */
public enum ArchivesOperationType {

    EMPLOY((byte) 0), TRANSFER((byte) 1), DISMISS((byte)2);
    private Byte code;

    private ArchivesOperationType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public static ArchivesOperationType fromCode(Byte code) {
        if (code != null) {
            for (ArchivesOperationType a : ArchivesOperationType.values()) {
                if (code.byteValue() == a.getCode().byteValue()) {
                    return a;
                }
            }
        }
        return null;
    }
}
