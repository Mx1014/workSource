package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class GetShortMessageResponse {
    @ItemType(String.class)
    List<String> messages;

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
