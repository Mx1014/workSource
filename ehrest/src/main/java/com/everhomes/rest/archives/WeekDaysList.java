package com.everhomes.rest.archives;

/**
 * <ul>
 * <li>SUNDAY(1): 星期日</li>
 * <li>MONDAY(2): 星期一</li>
 * <li>TUESDAY(3): 星期二</li>
 * <li>WEDNESDAY(4): 星期三</li>
 * <li>THURSDAY(5): 星期四</li>
 * <li>FRIDAY(6): 星期五</li>
 * <li>SATURDAY(7): 星期六</li>
 * </ul>
 */
public enum WeekDaysList {
    SUNDAY(1),
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6),
    SATURDAY(7);

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
