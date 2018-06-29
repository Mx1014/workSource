package com.everhomes.rest.meeting;

import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>MEMBER_DETAIL("MEMBER_DETAIL"): 个人</li>
 * </ul>
 */
public enum MeetingMemberSourceType {
    MEMBER_DETAIL("MEMBER_DETAIL");

    private String code;

    MeetingMemberSourceType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static MeetingMemberSourceType fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        MeetingMemberSourceType[] values = MeetingMemberSourceType.values();
        for (MeetingMemberSourceType value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
