package com.everhomes.rest.archives;

public enum WeekDaysList {
    MONDAY("monday"),
    TUESDAY("tuesday"),
    WEDNESDAY("wednesday"),
    THURSDAY("thursday"),
    FRIDAY("friday"),
    STAURDAY("staurday"),
    SUNDAY("sunday");

    private String code;

    private WeekDaysList(String code) {
        this.code = code;
    }

    public static WeekDaysList formCode(String code) {
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
