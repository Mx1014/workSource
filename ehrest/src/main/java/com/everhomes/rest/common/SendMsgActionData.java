package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为OPEN_MSG_SESSION时跳转到帖子详情
 * <li>dstChannel: 渠道类型，如USER、GROUP</li>
 * <li>dstChannelId: 渠道ID</li>
 * <li>srcChannel: 渠道类型，如USER、GROUP</li>
 * <li>srcChannelId: 渠道ID</li>
 * <li>senderUid: 发送者 User ID</li>
 * </ul>
 */
public class SendMsgActionData implements Serializable{

    private static final long serialVersionUID = -6207618272936404938L;
    //{"dstChannel": "user","dstChannelId":1}  
    private String dstChannel;
    private Long dstChannelId;
    
    public String getDstChannel() {
        return dstChannel;
    }

    public void setDstChannel(String dstChannel) {
        this.dstChannel = dstChannel;
    }

    public Long getDstChannelId() {
        return dstChannelId;
    }

    public void setDstChannelId(Long dstChannelId) {
        this.dstChannelId = dstChannelId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
