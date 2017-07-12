// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>name: 名称</li>
 *     <li>avatar: 头像</li>
 *     <li>messageType: 用户发送的消息类型，决定了客户端怎么显示这个用户发送的消息{@link com.everhomes.rest.messaging.UserMessageType}</li>
 *     <li>muteFlag: 免打扰状态{@link com.everhomes.rest.user.UserMuteNotificationFlag}</li>
 * </ul>
 */
public class MessageSessionInfoDTO {

    private String name;
    private String avatar;
    private String messageType;
    private Byte muteFlag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Byte getMuteFlag() {
        return muteFlag;
    }

    public void setMuteFlag(Byte muteFlag) {
        this.muteFlag = muteFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
