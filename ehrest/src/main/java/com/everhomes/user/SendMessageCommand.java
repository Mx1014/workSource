// @formatter:off
package com.everhomes.user;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>appId: 应用ID</li>
 * <li>deliveryOption: 消息发送类型。参考{@link com.everhomes.messaging.MessagingService}</li>
 * <li>messageJson: 实际消息实体的Json数据。参考{@link com.everhomes.messaging.MessageDTO}</li>
 * </ul>
 */
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
