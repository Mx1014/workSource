// @formatter:off
package com.everhomes.rest.messaging;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>channelType: 通道类型，目前支持 user/group</li>
 * <li>channelToken: 通道认证信息 uid/groupId</li>
 * </ul>
 *
 */
public class MessageChannel {
    private String channelType;
    private String channelToken;
    
    public MessageChannel() {
    }
    
    public MessageChannel(String channelType, String channelToken) {
        this.channelType = channelType;
        this.channelToken = channelToken;
    }
    
    public String getChannelType() {
        return channelType;
    }
    
    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
    
    public String getChannelToken() {
        return channelToken;
    }
    
    public void setChannelToken(String channelToken) {
        this.channelToken = channelToken;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
