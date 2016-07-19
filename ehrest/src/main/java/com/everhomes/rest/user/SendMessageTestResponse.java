package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class SendMessageTestResponse {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
