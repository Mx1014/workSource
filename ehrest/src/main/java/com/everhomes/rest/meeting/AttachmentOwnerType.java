package com.everhomes.rest.meeting;

public enum AttachmentOwnerType {
    EhMeetingReservations("EhMeetingReservations"), EhMeetingRecords("EhMeetingRecords"), EhMeetingTemplates("EhMeetingTemplates");
    private String code;

    AttachmentOwnerType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static AttachmentOwnerType fromCode(String code) {
        if (code != null) {
            AttachmentOwnerType[] values = AttachmentOwnerType.values();
            for (AttachmentOwnerType value : values) {
                if (code.equals(value.getCode())) {
                    return value;
                }
            }
        }
        return null;
    }
}
