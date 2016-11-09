package com.everhomes.rest.dbsync;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

public class CreateSyncMappingCommand {
    private String     name;
    private Long     syncAppId;
    private String     content;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getSyncAppId() {
        return syncAppId;
    }
    public void setSyncAppId(Long syncAppId) {
        this.syncAppId = syncAppId;
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
