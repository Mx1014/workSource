// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>
 *     <li>FORUM("forum"): 论坛</li>
 *     <li>ACTIVITY("activity"): ACTIVITY</li>
 *     <li>ANNOUNCEMENT("announcement"): ANNOUNCEMENT</li>
 * </ul>
 */
public enum InteractSettingType {
    FORUM("forum"), ACTIVITY("activity"), ANNOUNCEMENT("announcement");

    private String code;

    private InteractSettingType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static InteractSettingType fromCode(String code) {
        if (code != null) {
            InteractSettingType[] values = InteractSettingType.values();
            for (InteractSettingType value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
