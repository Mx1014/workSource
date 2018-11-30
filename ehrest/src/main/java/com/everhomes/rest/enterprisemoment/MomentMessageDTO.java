package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>enterpriseMomentId: 动态id</li>
 * <li>messageType: 内容类型 1-点赞 2-评论，参考{@link com.everhomes.rest.enterprisemoment.MessageType}</li>
 * <li>message: 内容</li>
 * <li>sourceReplyToUserId: 消息对应评论回复人的uid</li>
 * <li>operatorUid: 操作人uid</li>
 * <li>operatorName: 操作人名称</li>
 * <li>operateTime: 操作时间</li>
 * <li>operatorAvatarUrl: 操作人头像</li>
 * <li>momentContent: 动态内容</li>
 * <li>momentAttachmentUrl: 动态第一张图片,如果为空则显示动态内容</li>
 * <li>sourceDeleteFlag: (评论/点赞)删除标记</li>
 * <li>momentDeleteFlag: (动态)删除标记</li>
 * </ul>
 */

public class MomentMessageDTO {
    private Long id;
    private Long enterpriseMomentId;
    private Byte messageType;
    private String message;
    private Long sourceReplyToUserId;
    private String operatorName;
    private Long operatorUid;
    private Long operateTime;
    private String operatorAvatarUrl;
    private String momentContent;
    private String momentAttachmentUrl;
    private Byte momentDeleteFlag;
    private Byte sourceDeleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnterpriseMomentId() {
        return enterpriseMomentId;
    }

    public void setEnterpriseMomentId(Long enterpriseMomentId) {
        this.enterpriseMomentId = enterpriseMomentId;
    }

    public Byte getMessageType() {
        return messageType;
    }

    public void setMessageType(Byte messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSourceReplyToUserId() {
        return sourceReplyToUserId;
    }

    public void setSourceReplyToUserId(Long sourceReplyToUserId) {
        this.sourceReplyToUserId = sourceReplyToUserId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public Long getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Long operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperatorAvatarUrl() {
        return operatorAvatarUrl;
    }

    public void setOperatorAvatarUrl(String operatorAvatarUrl) {
        this.operatorAvatarUrl = operatorAvatarUrl;
    }

    public String getMomentContent() {
        return momentContent;
    }

    public void setMomentContent(String momentContent) {
        this.momentContent = momentContent;
    }

    public String getMomentAttachmentUrl() {
        return momentAttachmentUrl;
    }

    public void setMomentAttachmentUrl(String momentAttachmentUrl) {
        this.momentAttachmentUrl = momentAttachmentUrl;
    }

    public Byte getMomentDeleteFlag() {
        return momentDeleteFlag;
    }

    public void setMomentDeleteFlag(Byte momentDeleteFlag) {
        this.momentDeleteFlag = momentDeleteFlag;
    }

    public Byte getSourceDeleteFlag() {
        return sourceDeleteFlag;
    }

    public void setSourceDeleteFlag(Byte sourceDeleteFlag) {
        this.sourceDeleteFlag = sourceDeleteFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
