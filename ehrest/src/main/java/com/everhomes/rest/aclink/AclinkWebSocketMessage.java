package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class AclinkWebSocketMessage {
    private Long id;
    private Long seq;
    private Integer type;
    private String payload;
    private String uuid;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getSeq() {
        return seq;
    }
    public void setSeq(Long seq) {
        this.seq = seq;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public String getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }
    
    
    public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
