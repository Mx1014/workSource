package com.everhomes.rest.pmtask;

/**
 * Created by ying.xiong on 2017/7/14.
 */
public enum TaskRemarkSource {
    ZJGK("zjgk");

    private String code;
    private TaskRemarkSource(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static TaskRemarkSource fromCode(String code) {
        if(code != null) {
            TaskRemarkSource[] values = TaskRemarkSource.values();
            for(TaskRemarkSource value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
