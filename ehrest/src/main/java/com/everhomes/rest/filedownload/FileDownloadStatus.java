package com.everhomes.rest.filedownload;

/**
 * <ul>
 *     <li>WAITING((byte) 0): WAITING</li>
 *     <li>RUNNING((byte) 1): RUNNING</li>
 *     <li>SUCCESS((byte) 2): SUCCESS</li>
 *     <li>CANCEL((byte) 3): CANCEL</li>
 *     <li>FAIL((byte) 4): FAIL</li>
 * </ul>
 */
public enum FileDownloadStatus {

    WAITING((byte) 0), RUNNING((byte) 1), SUCCESS((byte) 2), CANCEL((byte) 3), FAIL((byte) 4);

    private Byte core;

    private FileDownloadStatus(Byte core) {
        this.core = core;
    }

    public Byte getCore() {
        return core;
    }

    public static FileDownloadStatus fromName(Byte core) {
        if (core != null) {
            FileDownloadStatus[] values = FileDownloadStatus.values();
            for (FileDownloadStatus value : values) {
                if (value.equals(core)) {
                    return value;
                }
            }
        }

        return null;
    }
}
