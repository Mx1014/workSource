package com.everhomes.rest.messaging.admin;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

public class SendMessageAdminCommand {
    @ItemType(String.class)
    private Map<String, String> meta;
    
    @NotNull
    private String bodyType;
    
    @NotNull
    private String body;
    
    @NotNull
    private Integer deliveryOption;
    
    @NotNull
    private Integer targetType;
    
    @NotNull
    private Long targetToken;
    
    public Map<String, String> getMeta() {
        return meta;
    }
    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }
    public String getBodyType() {
        return bodyType;
    }
    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public Integer getDeliveryOption() {
        return deliveryOption;
    }
    public void setDeliveryOption(Integer deliveryOption) {
        this.deliveryOption = deliveryOption;
    }
    public Integer getTargetType() {
        return targetType;
    }
    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }
    public Long getTargetToken() {
        return targetToken;
    }
    public void setTargetToken(Long targetToken) {
        this.targetToken = targetToken;
    }
    
}
