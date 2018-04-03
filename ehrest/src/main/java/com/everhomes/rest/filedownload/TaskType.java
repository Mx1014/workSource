package com.everhomes.rest.filedownload;

/**
 * <ul>
 *     <li>DEFAULT((byte) 0): DEFAULT</li>
 *     <li>FILEDOWNLOAD((byte) 1): FILEDOWNLOAD</li>
 * </ul>
 */
public enum TaskType {

    DEFAULT((byte) 0), FILEDOWNLOAD((byte) 1);

    private Byte core;

    private TaskType(Byte core) {
        this.core = core;
    }

    public Byte getCode() {
        return core;
    }

    public static TaskType fromName(Byte core) {
        if (core != null) {
            TaskType[] values = TaskType.values();
            for (TaskType value : values) {
                if (value.equals(core)) {
                    return value;
                }
            }
        }

        return null;
    }
}
