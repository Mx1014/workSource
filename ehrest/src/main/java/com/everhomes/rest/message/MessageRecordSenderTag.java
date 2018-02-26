package com.everhomes.rest.message;

public enum  MessageRecordSenderTag {

    FORWARD_EVENT("FORWARD EVENT"), NOTIFY_EVENT("NOTIFY EVENT"), APPIDSTATUS("APPIDSTATUS"), REGISTER_LOGIN("REGISTER LOGIN"), NOTIFY_REQUEST("NOTIFY REQUEST"), FETCH_PASTTORECENT_MESSAGES("FETCH PASTTORECENT MESSAGES"), ROUTE_STORE_MESSAGE("ROUTE STORE MESSAGE"), ROUTE_REALTIME_MESSAGE("ROUTE REALTIME MESSAGE"), FETCH_NOTIFY_MESSAGES("FETCH NOTIFY MESSAGES"), ROUTE_MESSAGE("ROUTE MESSAGE");

    private String code;
    private MessageRecordSenderTag(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static MessageRecordSenderTag fromCode(String code) {
        MessageRecordSenderTag[] values = MessageRecordSenderTag.values();
        for(MessageRecordSenderTag value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }

}
