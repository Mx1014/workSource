package com.everhomes.rest.message;

import com.everhomes.util.StringHelper;

public class PersistMessageRecordCommand {

    private String messageRecordDto;

    private String sessionToken;

    public String getMessageRecordDto() {
        return messageRecordDto;
    }

    public void setMessageRecordDto(String messageRecordDto) {
        this.messageRecordDto = messageRecordDto;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
