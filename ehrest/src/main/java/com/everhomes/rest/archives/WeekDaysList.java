package com.everhomes.rest.archives;

/**
 * <ul>
 * <li>MONDAY(1): 星期一</li>
 * <li>TUESDAY(2): 星期二</li>
 * <li>WEDNESDAY(3): 星期三</li>
 * <li>THURSDAY(4): 星期四</li>
 * <li>FRIDAY(5): 星期五</li>
 * <li>SATURDAY(6): 星期六</li>
 * <li>SUNDAY(7): 星期日</li>
 * </ul>
 */
public enum WeekDaysList {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private Integer code;

    private WeekDaysList(Integer code) {
        this.code = code;
    }

    public static WeekDaysList formCode(Integer code) {
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
