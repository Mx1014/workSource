package com.everhomes.rest.filedownload;

/**
 * <ul>
 *     <li>NO((byte) 0): 不允许重复执行</li>
 *     <li>rate_repeat((byte) 1): 按照比例重复执行</li>
 *     <li>repeat((byte) 2): 可重复执行</li>
 * </ul>
 */
public enum TaskRepeatFlag {

    NO((byte) 0), rate_repeat((byte) 1), repeat((byte) 2);

    private Byte core;

    private TaskRepeatFlag(Byte core) {
        this.core = core;
    }

    public Byte getCode() {
        return core;
    }

    public static TaskRepeatFlag fromName(Byte core) {
        if (core != null) {
            TaskRepeatFlag[] values = TaskRepeatFlag.values();
            for (TaskRepeatFlag value : values) {
                if (value.equals(core)) {
                    return value;
                }
            }
        }

        return null;
    }
}
