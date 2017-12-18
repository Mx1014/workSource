package com.everhomes.rest.filedownload;

/**
 * <ul>
 *     <li>NO((byte) 0): 不允许重复执行</li>
 *     <li>rate_repeat((byte) 1): 按照比例重复执行</li>
 *     <li>repeat((byte) 2): 可重复执行</li>
 * </ul>
 */
public enum JobRepeatFlag {

    NO((byte) 0), rate_repeat((byte) 1), repeat((byte) 2);

    private Byte core;

    private JobRepeatFlag(Byte core) {
        this.core = core;
    }

    public Byte getCode() {
        return core;
    }

    public static JobRepeatFlag fromName(Byte core) {
        if (core != null) {
            JobRepeatFlag[] values = JobRepeatFlag.values();
            for (JobRepeatFlag value : values) {
                if (value.equals(core)) {
                    return value;
                }
            }
        }

        return null;
    }
}
