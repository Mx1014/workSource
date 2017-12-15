package com.everhomes.rest.filedownload;

/**
 * <ul>
 *     <li>DEFAULT((byte) 0): DEFAULT</li>
 *     <li>FILEDOWNLOAD((byte) 1): FILEDOWNLOAD</li>
 * </ul>
 */
public enum JobType {

    DEFAULT((byte) 0), FILEDOWNLOAD((byte) 1);

    private Byte core;

    private JobType(Byte core) {
        this.core = core;
    }

    public Byte getCode() {
        return core;
    }

    public static JobType fromName(Byte core) {
        if (core != null) {
            JobType[] values = JobType.values();
            for (JobType value : values) {
                if (value.equals(core)) {
                    return value;
                }
            }
        }

        return null;
    }
}
