package com.everhomes.rest.rpc.server;

import com.everhomes.util.Name;

@Name("aclink.remote")
public class AclinkRemotePdu {
    private String uuid;
    private Integer type;
    private String body;
    
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
}
