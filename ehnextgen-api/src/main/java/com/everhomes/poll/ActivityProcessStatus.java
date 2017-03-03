package com.everhomes.poll;

/**
 * 
 * <ul>
 * <li>UNKNOWN: 0，未知</li>
 * <li>SINGING_UP: 1，报名进行中</li>
 * <li>UNDERWAY: 2， 活动进行中</li>
 * <li>END: 3，活动已结束</li>
 * <li>NOTSTART: 4，活动未开始</li>
 * </ul>
 */
public enum ActivityProcessStatus {
    UNKNOWN(0), SINGING_UP(1), UNDERWAY(2), END(3), NOTSTART(4);
    private Integer code;

    private ActivityProcessStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static ActivityProcessStatus fromString(String str) {
        for (ActivityProcessStatus status : ActivityProcessStatus.values()) {
            if (status.name().equalsIgnoreCase(str)) {
                return status;
            }
        }
        return UNKNOWN;
    }

    public static ActivityProcessStatus fromCode(Integer code) {
        for (ActivityProcessStatus status : ActivityProcessStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return UNKNOWN;
    }
}
