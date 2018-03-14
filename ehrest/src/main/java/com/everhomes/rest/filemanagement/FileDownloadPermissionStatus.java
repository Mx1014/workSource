package com.everhomes.rest.filemanagement;

/**
 * <ul>
 * <li>REFUSE((byte) 0): 拒绝下载</li>
 * <li>ALLOW((byte) 1): 允许下载</li>
 * </ul>
 */
public enum FileDownloadPermissionStatus {
    REFUSE((byte) 0), ALLOW((byte) 1);

    private byte code;

    private FileDownloadPermissionStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static FileDownloadPermissionStatus fromCode(Byte code) {
        if (code != null) {
            FileDownloadPermissionStatus[] values = FileDownloadPermissionStatus.values();
            for (FileDownloadPermissionStatus value : values) {
                if (code.byteValue() == value.code)
                    return value;
            }
        }
        return null;
    }
}
