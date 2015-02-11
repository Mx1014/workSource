// @formatter:off
package com.everhomes.user;

import javax.validation.constraints.NotNull;

public class SendMessageCommand {
    private Long appId;
    private Integer deliveryOption;
    
    @NotNull
    private String messageJson;
    
    public SendMessageCommand() {
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Integer getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(Integer deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public String getMessageJson() {
        return messageJson;
    }

    public void setMessageJson(String messageJson) {
        this.messageJson = messageJson;
    }
}
