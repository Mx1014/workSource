package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>feedbackType:1-举报、2-投诉、3-纠正</li>
 *  <li>targetType:0-无具体目标、1-帖子内容、2-地址、3-圈子</li>
 *  <li>targetId:所针对的目标对应的ID（如地址对应address id、帖子内容则是post id)</li>
 *  <li>contentCategory: 参考{@link com.everhomes.rest.user.FeedbackContentCategoryType}</li>
 *  <li>contact:联系方式</li>
 *  <li>subject:反馈标题</li>
 *  <li>content:反馈内容</li>
 *  <li>proofResourceUri:图片链接</li>
 * </ul>
 */
public class FeedbackCommand {
    private Byte feedbackType;
    private Byte targetType;
    private Long targetId;
    private Long contentCategory;
    private String contact;
    private String subject;
    private String content;

    private String proofResourceUri;

    public Byte getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Byte feedbackType) {
        this.feedbackType = feedbackType;
    }

    public Byte getTargetType() {
        return targetType;
    }

    public void setTargetType(Byte targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(Long contentCategory) {
        this.contentCategory = contentCategory;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProofResourceUri() {
        return proofResourceUri;
    }

    public void setProofResourceUri(String proofResourceUri) {
        this.proofResourceUri = proofResourceUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
