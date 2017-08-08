package com.everhomes.rest.openapi;

import javax.validation.constraints.NotNull;

public class BusinessMessageV2Command {
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
    
    private String meta;
    
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

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public Byte getBizMessageType() {
		return bizMessageType;
	}

	public void setBizMessageType(Byte bizMessageType) {
		this.bizMessageType = bizMessageType;
	}
    
}
