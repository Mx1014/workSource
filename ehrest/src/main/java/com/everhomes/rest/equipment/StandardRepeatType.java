package com.everhomes.rest.equipment;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>0: no_repeating</li>
 * <li>1: by day </li>
 * <li>1: by week </li>
 * <li>1: by month </li>
 * <li>1: by year </li>
 * </ul>
 */
public enum StandardRepeatType {
    NO_REPEAT((byte) 0, "不重复"), BY_DAY((byte) 1, "按日"), BY_WEEK((byte) 2, "按周"), BY_MONTH((byte) 3, "按月"), BY_YEAR((byte) 4, "按年");

    private byte code;
    private String name;

    StandardRepeatType(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static StandardRepeatType fromStatus(byte code) {
        for (StandardRepeatType value : StandardRepeatType.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
