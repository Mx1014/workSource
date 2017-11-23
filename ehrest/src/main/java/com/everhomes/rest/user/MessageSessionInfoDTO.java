// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>name: 名称</li>
 *     <li>avatar: 头像</li>
 *     <li>messageType: 用户发送的消息类型，决定了客户端怎么显示这个用户发送的消息{@link com.everhomes.rest.messaging.UserMessageType}</li>
 *     <li>muteFlag: 免打扰状态{@link com.everhomes.rest.user.UserMuteNotificationFlag}</li>
 *     <li>alias: group别名，如果name为空的时候会返回一个别名：用户1、用户2、用户3、用户4、用户5</li>
 *     <li>isNameEmptyBefore: 原来的name字段是否为空，0-非空，1-空, {@link com.everhomes.rest.group.GroupNameEmptyFlag}</li>
 * </ul>
 */
public class MessageSessionInfoDTO {

    private String name;
    private String avatar;
    private String messageType;
    private Byte muteFlag;
    private String alias;
    private Byte isNameEmptyBefore;

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Byte getIsNameEmptyBefore() {
        return isNameEmptyBefore;
    }

    public void setIsNameEmptyBefore(Byte isNameEmptyBefore) {
        this.isNameEmptyBefore = isNameEmptyBefore;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
