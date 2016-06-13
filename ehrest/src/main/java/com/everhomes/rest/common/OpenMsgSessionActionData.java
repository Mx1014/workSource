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
public class OpenMsgSessionActionData implements Serializable{

    private static final long serialVersionUID = 7629778348885855002L;
    //{"dstChannel": "user","dstChannelId":1,"srcChannel":"user","srcChannelId":1,"senderUid":1}  
    private String dstChannel;
    private Long dstChannelId;
    private String srcChannel;
    private Long srcChannelId;
    private Long senderUid;
    
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

    public String getSrcChannel() {
        return srcChannel;
    }

    public void setSrcChannel(String srcChannel) {
        this.srcChannel = srcChannel;
    }

    public Long getSrcChannelId() {
        return srcChannelId;
    }

    public void setSrcChannelId(Long srcChannelId) {
        this.srcChannelId = srcChannelId;
    }

    public Long getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(Long senderUid) {
        this.senderUid = senderUid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
