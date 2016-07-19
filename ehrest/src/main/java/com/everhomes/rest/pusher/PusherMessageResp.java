package com.everhomes.rest.pusher;

import com.everhomes.util.Name;
import com.everhomes.util.StringHelper;

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
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
