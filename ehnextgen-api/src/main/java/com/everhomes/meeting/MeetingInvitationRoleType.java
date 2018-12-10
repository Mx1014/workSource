package com.everhomes.meeting;


import org.apache.commons.lang.StringUtils;

public enum MeetingInvitationRoleType {
    ATTENDEE("参会人"), CC("抄送人"), MEETING_MANAGER("会务人");

    private String code;

    MeetingInvitationRoleType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static MeetingInvitationRoleType fromCode(String code) {
        if (!StringUtils.isBlank(code)) {
            MeetingInvitationRoleType[] values = MeetingInvitationRoleType.values();
            for (MeetingInvitationRoleType value : values) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
        }
        return null;
    }

}
