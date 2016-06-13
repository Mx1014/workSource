package com.everhomes.rest.organization;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDTO;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 	<li>task对象参数说明，参考 {@link com.everhomes.rest.organization.OrganizationTaskDTO}，其中，task的id为taskId，task的创建用户为taskCreatorUid，创建时间为taskCreateTime</li>
 * 	<li>PostDTO对象参数说明，参考 {@link com.everhomes.rest.forum.PostDTO}</li>
 *  <li>assignStatus: 0-未分配; 1-已分配</li>
 * </ul>
 *
 */
public class OrganizationTaskDTO2 {
	//task
	private java.lang.Long     taskId;
	private java.lang.Long     organizationId;
	private java.lang.String   organizationType;
	private java.lang.String   applyEntityType;
	private java.lang.Long     applyEntityId;
	private java.lang.String   targetType;
	private java.lang.Long     targetId;
	private java.lang.String   taskType;
	private java.lang.String   description;
	private java.lang.Byte     taskStatus;
	private java.lang.Long     operatorUid;
	private java.sql.Timestamp operateTime;
	private java.lang.Long     taskCreatorUid;
	private java.sql.Timestamp taskCreateTime;
	private int assignStatus;
	
	private java.sql.Timestamp unprocessedTime;
	private java.sql.Timestamp processingTime;
	private java.sql.Timestamp processedTime;
	private String taskCategory;

	//PostDTO
	private Long id;

	private String uuid;

	private Long parentPostId;

	private Long forumId;

	private Long creatorUid;

	private String creatorNickName;

	private String creatorAvatar;

	private String creatorAvatarUrl;

	private Byte creatorAdminFlag;

	private String creatorTag;

	private String targetTag;    

	private Long contentCategory;

	private Long actionCategory;

	private Byte visibleRegionType;

	private Long visibleRegionId;

	private Long communityId;

	private Double longitude;

	private Double latitude;

	private String subject;

	private String contentType;

	private String content;

	private Long embeddedAppId;

	private Long embeddedId;

	// json encoded List<String> 
	private String embeddedJson;

	private Byte isForwarded;

	private Long childCount;

	private Long forwardCount;

	private Long likeCount;

	private Long dislikeCount;

	private Long viewCount;

	private Timestamp updateTime;

	private Timestamp createTime;

	@ItemType(AttachmentDTO.class)
	private List<AttachmentDTO> attachments;

	private Byte assignedFlag;

	private String forumName;

	private Byte likeFlag;

	private String shareUrl;

	private Byte privateFlag;

	public int getAssignStatus() {
		return assignStatus;
	}

	public void setAssignStatus(int assignStatus) {
		this.assignStatus = assignStatus;
	}

	public java.lang.Long getTaskId() {
		return taskId;
	}

	public void setTaskId(java.lang.Long taskId) {
		this.taskId = taskId;
	}

	public java.lang.Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(java.lang.Long organizationId) {
		this.organizationId = organizationId;
	}

	public java.lang.String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(java.lang.String organizationType) {
		this.organizationType = organizationType;
	}

	public java.lang.String getApplyEntityType() {
		return applyEntityType;
	}

	public void setApplyEntityType(java.lang.String applyEntityType) {
		this.applyEntityType = applyEntityType;
	}

	public java.lang.Long getApplyEntityId() {
		return applyEntityId;
	}

	public void setApplyEntityId(java.lang.Long applyEntityId) {
		this.applyEntityId = applyEntityId;
	}

	public java.lang.String getTargetType() {
		return targetType;
	}

	public void setTargetType(java.lang.String targetType) {
		this.targetType = targetType;
	}

	public java.lang.Long getTargetId() {
		return targetId;
	}

	public void setTargetId(java.lang.Long targetId) {
		this.targetId = targetId;
	}

	public java.lang.String getTaskType() {
		return taskType;
	}

	public void setTaskType(java.lang.String taskType) {
		this.taskType = taskType;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.Byte getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(java.lang.Byte taskStatus) {
		this.taskStatus = taskStatus;
	}

	public java.lang.Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(java.lang.Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public java.sql.Timestamp getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(java.sql.Timestamp operateTime) {
		this.operateTime = operateTime;
	}

	public java.lang.Long getTaskCreatorUid() {
		return taskCreatorUid;
	}

	public void setTaskCreatorUid(java.lang.Long taskCreatorUid) {
		this.taskCreatorUid = taskCreatorUid;
	}

	public java.sql.Timestamp getTaskCreateTime() {
		return taskCreateTime;
	}

	public void setTaskCreateTime(java.sql.Timestamp taskCreateTime) {
		this.taskCreateTime = taskCreateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getParentPostId() {
		return parentPostId;
	}

	public void setParentPostId(Long parentPostId) {
		this.parentPostId = parentPostId;
	}

	public Long getForumId() {
		return forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public String getCreatorNickName() {
		return creatorNickName;
	}

	public void setCreatorNickName(String creatorNickName) {
		this.creatorNickName = creatorNickName;
	}

	public String getCreatorAvatar() {
		return creatorAvatar;
	}

	public void setCreatorAvatar(String creatorAvatar) {
		this.creatorAvatar = creatorAvatar;
	}

	public String getCreatorAvatarUrl() {
		return creatorAvatarUrl;
	}

	public void setCreatorAvatarUrl(String creatorAvatarUrl) {
		this.creatorAvatarUrl = creatorAvatarUrl;
	}

	public Byte getCreatorAdminFlag() {
		return creatorAdminFlag;
	}

	public void setCreatorAdminFlag(Byte creatorAdminFlag) {
		this.creatorAdminFlag = creatorAdminFlag;
	}

	public String getCreatorTag() {
		return creatorTag;
	}

	public void setCreatorTag(String creatorTag) {
		this.creatorTag = creatorTag;
	}

	public String getTargetTag() {
		return targetTag;
	}

	public void setTargetTag(String targetTag) {
		this.targetTag = targetTag;
	}

	public Long getContentCategory() {
		return contentCategory;
	}

	public void setContentCategory(Long contentCategory) {
		this.contentCategory = contentCategory;
	}

	public Long getActionCategory() {
		return actionCategory;
	}

	public void setActionCategory(Long actionCategory) {
		this.actionCategory = actionCategory;
	}

	public Byte getVisibleRegionType() {
		return visibleRegionType;
	}

	public void setVisibleRegionType(Byte visibleRegionType) {
		this.visibleRegionType = visibleRegionType;
	}

	public Long getVisibleRegionId() {
		return visibleRegionId;
	}

	public void setVisibleRegionId(Long visibleRegionId) {
		this.visibleRegionId = visibleRegionId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getEmbeddedAppId() {
		return embeddedAppId;
	}

	public void setEmbeddedAppId(Long embeddedAppId) {
		this.embeddedAppId = embeddedAppId;
	}

	public Long getEmbeddedId() {
		return embeddedId;
	}

	public void setEmbeddedId(Long embeddedId) {
		this.embeddedId = embeddedId;
	}

	public String getEmbeddedJson() {
		return embeddedJson;
	}

	public void setEmbeddedJson(String embeddedJson) {
		this.embeddedJson = embeddedJson;
	}

	public Byte getIsForwarded() {
		return isForwarded;
	}

	public void setIsForwarded(Byte isForwarded) {
		this.isForwarded = isForwarded;
	}

	public Long getChildCount() {
		return childCount;
	}

	public void setChildCount(Long childCount) {
		this.childCount = childCount;
	}

	public Long getForwardCount() {
		return forwardCount;
	}

	public void setForwardCount(Long forwardCount) {
		this.forwardCount = forwardCount;
	}

	public Long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Long likeCount) {
		this.likeCount = likeCount;
	}

	public Long getDislikeCount() {
		return dislikeCount;
	}

	public void setDislikeCount(Long dislikeCount) {
		this.dislikeCount = dislikeCount;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<AttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public Byte getAssignedFlag() {
		return assignedFlag;
	}

	public void setAssignedFlag(Byte assignedFlag) {
		this.assignedFlag = assignedFlag;
	}

	public String getForumName() {
		return forumName;
	}

	public void setForumName(String forumName) {
		this.forumName = forumName;
	}

	public Byte getLikeFlag() {
		return likeFlag;
	}

	public void setLikeFlag(Byte likeFlag) {
		this.likeFlag = likeFlag;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public Byte getPrivateFlag() {
		return privateFlag;
	}

	public void setPrivateFlag(Byte privateFlag) {
		this.privateFlag = privateFlag;
	}
	
	public java.sql.Timestamp getUnprocessedTime() {
		return unprocessedTime;
	}

	public void setUnprocessedTime(java.sql.Timestamp unprocessedTime) {
		this.unprocessedTime = unprocessedTime;
	}

	public java.sql.Timestamp getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(java.sql.Timestamp processingTime) {
		this.processingTime = processingTime;
	}

	public java.sql.Timestamp getProcessedTime() {
		return processedTime;
	}

	public void setProcessedTime(java.sql.Timestamp processedTime) {
		this.processedTime = processedTime;
	}
	
	

	public String getTaskCategory() {
		return taskCategory;
	}

	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
