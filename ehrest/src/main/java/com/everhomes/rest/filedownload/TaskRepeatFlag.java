package com.everhomes.rest.filedownload;

/**
 * <ul>
 *     <li>NO((byte) 0): 不允许重复执行</li>
 *     <li>rate_repeat((byte) 1): 按照比例重复执行(暂未启用)</li>
 *     <li>repeat((byte) 2): 可重复执行</li>
 * </ul>
 */
public enum TaskRepeatFlag {

    NO((byte) 0), RATE_REPEAT((byte) 1), REPEAT((byte) 2);

    private Byte code;

    private TaskRepeatFlag(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static TaskRepeatFlag fromCode(Byte code) {
        if (code != null) {
            TaskRepeatFlag[] values = TaskRepeatFlag.values();
            for (TaskRepeatFlag value : values) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
