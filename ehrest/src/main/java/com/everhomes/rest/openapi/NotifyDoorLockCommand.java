package com.everhomes.rest.openapi;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

public class NotifyDoorLockCommand {
    @NotNull
    private Long timestamp;
    
    @NotNull
    private Integer nonce;
    
    private String crypto;
    
    @NotNull
    @ItemType(String.class)
    private List<String> Phones;

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
    
}
