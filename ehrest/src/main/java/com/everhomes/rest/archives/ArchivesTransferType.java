package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>PROMOTE((byte) 0): 晋升</li>
 * <li>TRANSFER((byte) 1): 调整、安排</li>
 * <li>OTHER((byte) 2): 其他</li>
 * </ul>
 */
public enum ArchivesTransferType {
    PROMOTE((byte) 0), TRANSFER((byte) 1), OTHER((byte) 2);
    private Byte code;

    private ArchivesTransferType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public static ArchivesTransferType fromCode(Byte code) {
        if (code != null) {
            for (ArchivesTransferType a : ArchivesTransferType.values()) {
                if (code.byteValue() == a.getCode().byteValue()) {
                    return a;
                }
            }
        }
        return null;
    }
}
