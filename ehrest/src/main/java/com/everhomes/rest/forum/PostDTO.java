// @formatter:off
package com.everhomes.rest.forum;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.user.UserLikeType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 帖子或评论ID</li>
 *     <li>uuid: 帖子或评论UUID</li>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>parentPostId: 帖子或评论的父亲ID</li>
 *     <li>forumId: 论坛ID</li>
 *     <li>creatorUid: 创建者ID</li>
 *     <li>creatorNickName: 创建者在圈内的昵称</li>
 *     <li>creatorAvatar: 创建者在圈内的头像URI</li>
 *     <li>creatorAvatarUrl: 创建者在圈内的头像URL</li>
 *     <li>creatorAdminFlag: 创建者是否为圈的管理员</li>
 *     <li>creatorTag: 创建者标签，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 *     <li>creatorCommunityName: 创建者小区名称</li>
 *     <li>targetTag: 创建者标签，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 *     <li>contentCategory: 内容类型ID，含类和子类</li>
 *     <li>actionCategory: 操作类型ID，如拼车中的“我搭车”、“我开车”</li>
 *     <li>visibleRegionType: 区域范围类型，{@link com.everhomes.rest.visibility.VisibleRegionType}</li>
 *     <li>visibleRegionId: 区域范围类型对应的ID</li>
 *     <li>visibleRegionIds: 区域范围类型对应的IDs。新版的活动发布时出现了范围的概念，比如“园区A、园区C和小区A”。visibleRegionId和visibleRegionIds只传一个</li>
 *     <li>communityId: 用户当前小区ID</li>
 *     <li>longitude: 帖子或评论内容涉及到的经度如活动</li>
 *     <li>latitude: 帖子或评论内容涉及到的纬度如活动</li>
 *     <li>subject: 帖子或评论标题</li>
 *     <li>contentType: 帖子或评论内容类型，{@link com.everhomes.rest.forum.PostContentType}</li>
 *     <li>content: 帖子或评论内容</li>
 *     <li>embeddedAppId: 内嵌对象对应的App ID，{@link com.everhomes.rest.app.AppConstants}</li>
 *     <li>embeddedId: 内嵌对象ID</li>
 *     <li>embeddedJson: 内嵌对象列表对应的json字符串</li>
 *     <li>isForwarded: 是否是转发帖的标记</li>
 *     <li>childCount: 孩子数目，如帖子下的评论数目</li>
 *     <li>forwardCount: 帖子或评论的转发数目</li>
 *     <li>likeCount: 帖子或评论赞的数目</li>
 *     <li>dislikeCount: 帖子或评论踩的数目</li>
 *     <li>viewCount: 浏览的数目</li>
 *     <li>updateTime: 帖子或评论更新时间</li>
 *     <li>createTime: 帖子或评论创建时间</li>
 *     <li>attachments: 帖子或评论的附件信息，参见{@link com.everhomes.rest.forum.AttachmentDTO}</li>
 *     <li>assignedFlag: 是否推荐帖，参见{@link com.everhomes.rest.forum.PostAssignedFlag}</li>
 *     <li>forumName: forumName</li>
 *     <li>likeFlag: 是否点赞，参见{@link UserLikeType}</li>
 *     <li>favoriteFlag: 是否收藏标记，参见{@link com.everhomes.rest.forum.PostFavoriteFlag}</li>
 *     <li>shareUrl: 分享链接</li>
 *     <li>privateFlag: 帖子是否公开标记，应用场景：发给物业、政府相关部门的帖子默认不公开，由物业、政府相关部门决定是否公开；参考{@link com.everhomes.rest.forum.PostPrivacy}</li>
 *     <li>floorNumber: 楼层（仅用于评论）</li>
 *     <li>publishStatus: 帖子发布状态，{@link com.everhomes.rest.forum.TopicPublishStatus}</li>
 *     <li>startTime: 开始时间</li>
 *     <li>endTime: 结束时间</li>
 *     <li>mediaDisplayFlag: 是否显示图片，0否1是</li>
 *     <li>maxQuantity: 限制人数</li>
 *     <li>minQuantity: 最低限制人数</li>
 *     <li>contentUrl: 内容链接</li>
 *     <li>groupId: 俱乐部id</li>
 *     <li>groupName: 俱乐部名称</li>
 *     <li>status: 活动状态，0-已删除，1-待确认，2-正常。用于暂存或者立刻发布  参考{@link com.everhomes.rest.forum.PostStatus}</li>
 *     <li>ownerToken: ownerToken</li>
 *     <li>tag: tag</li>
 *     <li>cloneFlag: 克隆标识，参考{@link com.everhomes.rest.forum.PostCloneFlag}</li>
 *     <li>forumEntryId: 论坛应用入口Id</li>
 *     <li>interactFlag: 是否支持评论 0-no, 1-yes 参考{@link InteractFlag}</li>
 *	   <li>stickFlag: 置顶标志，0-否，1-是，参考{@link StickFlag}</li>
 *	   <li>stickTime: 置顶时间</li>
 *	   <li>moduleType: 模块类型，现在所有的帖子都要往帖子表里写，通过判断条件已经很难区分是哪里来的帖子了，现在由创建帖子的时候带来。 参考{@link ForumModuleType}</li>
 *     <li>moduleCategoryId: 业务模块的入口id</li>
 * </ul>
 */
public class PostDTO {
    private Long id;

    private String uuid;

    private Integer namespaceId;

    private Long parentPostId;

    private Long forumId;

    private Long creatorUid;

    private String creatorNickName;

    private String creatorAvatar;

    private String creatorAvatarUrl;

    private Byte creatorAdminFlag;

    private String creatorTag;

    private String creatorCommunityName;

    private String targetTag;

    private Long contentCategory;

    private Long actionCategory;

    private Byte visibleRegionType;

    private Long visibleRegionId;

    @ItemType(Long.class)
    private List<Long> visibleRegionIds;

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

    private Byte favoriteFlag;

    private String shareUrl;

    private Byte privateFlag;

    private Long floorNumber;

    private String publishStatus;

    private Long startTime;

    private Long endTime;

    private Byte mediaDisplayFlag;

    private Integer maxQuantity;

    private Integer minQuantity;

    private String contentUrl;

    private Long groupId;

    private String groupName;

    private Byte status;

    private String ownerToken;

    private String tag;

    private Byte cloneFlag;

    private Long forumEntryId;

    private Byte interactFlag;
	
	private Byte stickFlag;

    private Timestamp stickTime;

    private Byte moduleType;

    private Long moduleCategoryId;

    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Byte getMediaDisplayFlag() {
        return mediaDisplayFlag;
    }

    public void setMediaDisplayFlag(Byte mediaDisplayFlag) {
        this.mediaDisplayFlag = mediaDisplayFlag;
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public String getCreatorCommunityName() {
        return creatorCommunityName;
    }

    public void setCreatorCommunityName(String creatorCommunityName) {
        this.creatorCommunityName = creatorCommunityName;
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

    public Byte getFavoriteFlag() {
        return favoriteFlag;
    }

    public void setFavoriteFlag(Byte favoriteFlag) {
        this.favoriteFlag = favoriteFlag;
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

    public Long getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Long floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getOwnerToken() {
        return ownerToken;
    }

    public void setOwnerToken(String ownerToken) {
        this.ownerToken = ownerToken;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Long> getVisibleRegionIds() {
        return visibleRegionIds;
    }

    public void setVisibleRegionIds(List<Long> visibleRegionIds) {
        this.visibleRegionIds = visibleRegionIds;
    }

    public Byte getCloneFlag() {
        return cloneFlag;
    }

    public void setCloneFlag(Byte cloneFlag) {
        this.cloneFlag = cloneFlag;
    }

    public Long getForumEntryId() {
        return forumEntryId;
    }

    public void setForumEntryId(Long forumEntryId) {
        this.forumEntryId = forumEntryId;
    }

    public Byte getInteractFlag() {
        return interactFlag;
    }

    public void setInteractFlag(Byte interactFlag) {
        this.interactFlag = interactFlag;
    }
	public Byte getStickFlag() {
        return stickFlag;
    }

    public void setStickFlag(Byte stickFlag) {
        this.stickFlag = stickFlag;
    }

    public Timestamp getStickTime() {
        return stickTime;
    }

    public void setStickTime(Timestamp stickTime) {
        this.stickTime = stickTime;
    }

    public Byte getModuleType() {
        return moduleType;
    }

    public void setModuleType(Byte moduleType) {
        this.moduleType = moduleType;
    }

    public Long getModuleCategoryId() {
        return moduleCategoryId;
    }

    public void setModuleCategoryId(Long moduleCategoryId) {
        this.moduleCategoryId = moduleCategoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
