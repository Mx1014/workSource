package com.everhomes.rest.openapi;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

public class NotifyDoorMessageCommand {
    @NotNull
    private Long timestamp;
    
    @NotNull
    private Integer nonce;
    
    private String crypto;
    
    @NotNull
    @ItemType(String.class)
    private List<String> Phones;
    
    @NotNull
    private String contentType;
    
    @NotNull
    private String content;

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

    public String getCrypto() {
        return crypto;
    }

    public void setCrypto(String crypto) {
        this.crypto = crypto;
    }

    public List<String> getPhones() {
        return Phones;
    }

    public void setPhones(List<String> phones) {
        Phones = phones;
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
    
    
}
