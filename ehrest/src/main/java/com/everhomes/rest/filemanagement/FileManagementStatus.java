package com.everhomes.rest.filemanagement;

/**
 * <ul>
 * <li>INVALID((byte) 0): 无效的</li>
 * <li>VALID((byte) 1): 有效的</li>
 * </ul>
 */
public enum FileManagementStatus {
    INVALID((byte) 0), VALID((byte) 1);

    private byte code;

    private FileManagementStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static FileManagementStatus fromCode(Byte code) {
        if (code != null) {
            FileManagementStatus[] values = FileManagementStatus.values();
            for (FileManagementStatus value : values) {
                if (code.byteValue() == value.code)
                    return value;
            }
        }
        return null;
    }
}
