package com.everhomes.poll;

public enum ProcessStatus {
    UNKNOWN(0), NOTSTART(1), UNDERWAY(2), END(3);
    private Integer code;

    ProcessStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static ProcessStatus fromString(String str) {
        for (ProcessStatus status : ProcessStatus.values()) {
            if (status.name().equalsIgnoreCase(str)) {
                return status;
            }
        }
        return UNKNOWN;
    }

    public static ProcessStatus fromCode(Integer code) {
        for (ProcessStatus status : ProcessStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return UNKNOWN;
    }
}
