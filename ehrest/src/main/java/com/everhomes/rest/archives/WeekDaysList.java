package com.everhomes.rest.archives;

public enum WeekDaysList {
    SUNDAY((byte)1),
    MONDAY((byte)2),
    TUESDAY((byte)3),
    WEDNESDAY((byte)4),
    THURSDAY((byte)5),
    FRIDAY((byte)6),
    SATURDAY((byte)7);

    private Byte code;

    private WeekDaysList(Byte code) {
        this.code = code;
    }

    public static WeekDaysList formCode(Byte code) {
        if (code != null) {
            WeekDaysList[] values = WeekDaysList.values();
            for (WeekDaysList value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
