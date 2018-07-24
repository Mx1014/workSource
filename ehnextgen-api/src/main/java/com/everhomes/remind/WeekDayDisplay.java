package com.everhomes.remind;


public enum WeekDayDisplay {

    每周一((byte) 1), 每周二((byte) 2), 每周三((byte) 3), 每周四((byte) 4), 每周五((byte) 5), 每周六((byte) 6), 每周日((byte) 7);

    private byte code;

    WeekDayDisplay(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static WeekDayDisplay fromCode(Byte code) {
        if (code == null) {
            return null;
        }

        WeekDayDisplay[] values = WeekDayDisplay.values();
        for (WeekDayDisplay value : values) {
            if (value.code == code.intValue()) {
                return value;
            }
        }
        return null;
    }

}
