package com.everhomes.rest.openapi;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

public class BusinessMessageCustomCommand {
    @NotNull
    private Long timestamp;
    
    @NotNull
    private Integer nonce;
    
    @NotNull
    private String contentType;
    
    @NotNull
    private String content;
    
    @NotNull
    private String type;
    
    @NotNull
    private String customJson;
    
    @NotNull
    private Integer namespaceId;
    
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


	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCustomJson() {
		return customJson;
	}

	public void setCustomJson(String customJson) {
		this.customJson = customJson;
	}

}
