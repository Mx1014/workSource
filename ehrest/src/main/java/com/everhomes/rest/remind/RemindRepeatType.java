package com.everhomes.rest.remind;

/**
 * <ul>
 * <li>NONE((byte)0,"无"): 0</li>
 * <li>DAILY((byte) 1, "每日"): 1</li>
 * <li>WEEKLY((byte) 2, "每周"): 2</>
 * <li>MONTHLY((byte) 3, "每月"): 3</li>
 * <li>YEARLY((byte) 4, "每年"): 4</li>
 * </ul>
 */
public enum RemindRepeatType {
    NONE((byte) 0, "无"), DAILY((byte) 1, "每日"), WEEKLY((byte) 2, "每周"), MONTHLY((byte) 3, "每月"), YEARLY((byte) 4, "每年");

    private byte code;
    private String displayName;

    RemindRepeatType(Byte code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public byte getCode() {
        return this.code;
    }

    public static RemindRepeatType fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        RemindRepeatType[] values = RemindRepeatType.values();
        for (RemindRepeatType value : values) {
            if (value.code == code.byteValue()) {
                return value;
            }
        }
        return null;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
