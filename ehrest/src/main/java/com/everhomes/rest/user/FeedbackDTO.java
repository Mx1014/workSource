// @formatter:off
package com.everhomes.rest.user;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *   <li>id: id</li>
 *   <li>ownerUid: 举报人ID</li>
 *   <li>ownerNickName: 举报人用户名</li>
 *   <li>contact: 联系方式</li>
 *   <li>subject: 反馈标题</li>
 *   <li>content: 反馈内容</li>
 *   <li>createTime: 创建时间</li>
 *   <li>feedbackType:1-举报、2-投诉、3-纠正</li>
 *   <li>targetType:0-无具体目标、1-帖子内容、2-地址、3-圈子</li>
 *   <li>targetId:所针对的目标对应的ID（如地址对应address id、帖子内容则是post id)</li>
 *   <li>targetSubject: 目标的标题名称</li>
 *   <li>forumId: 如果举报类型是post的话，返回post对应的forumId</li>
 *   <li>targetStatus: 目标的状态， 0-已删除、1-待确认、2-正常。参考{@link com.everhomes.rest.forum.PostStatus}</li>
 *   <li>proofResourceUri:图片链接</li>
 *   <li>contentCategory:内容类别。 参考{@link com.everhomes.rest.user.FeedbackContentCategoryType}</li>
 *   <li>contentCategoryText: contentCategory对应的内容</li>
 *   <li>status: 处理状态  0-未处理,</li>
 *   <li>verifyType: 核实情况  0-不属实， 1-属实</li>
 *   <li>handleType: 处理方式 0-无， 1-删除</li>
 *   <li>namespaceId: 域空间id</li>
 * </ul>
 */
public class FeedbackDTO {
	private Long id;
	private Long ownerUid;
	private String ownerNickName;
	private String contact;
    private String subject;
    private String content;
    private Timestamp createTime;
    private Byte feedbackType;
    private Byte targetType;
    private Long targetId;
    private String targetSubject;
    private Long forumId;
    private Byte targetStatus;
    private String proofResourceUri;
    private Long contentCategory;
    private String contentCategoryText;
    private Byte status;
    private Byte verifyType;
    private Byte handleType;
    private Integer namespaceId;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerUid() {
		return ownerUid;
	}

	public void setOwnerUid(Long ownerUid) {
		this.ownerUid = ownerUid;
	}

	public String getOwnerNickName() {
		return ownerNickName;
	}

	public void setOwnerNickName(String ownerNickName) {
		this.ownerNickName = ownerNickName;
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

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

	public String getTargetSubject() {
		return targetSubject;
	}

	public void setTargetSubject(String targetSubject) {
		this.targetSubject = targetSubject;
	}

	public Long getForumId() {
		return forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

	public Byte getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(Byte targetStatus) {
		this.targetStatus = targetStatus;
	}

	public String getProofResourceUri() {
		return proofResourceUri;
	}

	public void setProofResourceUri(String proofResourceUri) {
		this.proofResourceUri = proofResourceUri;
	}

	public Long getContentCategory() {
		return contentCategory;
	}

	public void setContentCategory(Long contentCategory) {
		this.contentCategory = contentCategory;
	}

	public String getContentCategoryText() {
		return contentCategoryText;
	}

	public void setContentCategoryText(String contentCategoryText) {
		this.contentCategoryText = contentCategoryText;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(Byte verifyType) {
		this.verifyType = verifyType;
	}

	public Byte getHandleType() {
		return handleType;
	}

	public void setHandleType(Byte handleType) {
		this.handleType = handleType;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
