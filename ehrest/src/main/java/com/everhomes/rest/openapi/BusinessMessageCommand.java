package com.everhomes.rest.openapi;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

public class BusinessMessageCommand {
    @NotNull
    private Long timestamp;
    
    @NotNull
    private Integer nonce;
    
    @NotNull
    private Long userId;
    
    @NotNull
    private String contentType;
    
    @NotNull
    private String content;
    
    @ItemType(String.class)
    private Map<String, String> meta = new HashMap<String, String>();
    
    private Byte bizMessageType;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

	public Byte getBizMessageType() {
		return bizMessageType;
	}

	public void setBizMessageType(Byte bizMessageType) {
		this.bizMessageType = bizMessageType;
	}
    
}
