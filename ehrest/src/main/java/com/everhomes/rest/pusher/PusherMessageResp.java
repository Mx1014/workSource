package com.everhomes.rest.pusher;

import com.everhomes.util.Name;

@Name("NOTIFY")
public class PusherMessageResp {
    private String name;
    private String content;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
    
}
