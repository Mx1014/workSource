// @formatter:off
package com.everhomes.rest.announcement;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.user.UserLikeType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id: 公告ID</li>
 *     <li>uuid: 公告UUID</li>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>creatorUid: 创建者ID</li>
 *     <li>creatorNickName: 创建者在圈内的昵称</li>
 *     <li>creatorAvatar: 创建者在圈内的头像URI</li>
 *     <li>creatorAvatarUrl: 创建者在圈内的头像URL</li>
 *     <li>creatorAdminFlag: 创建者是否为圈的管理员</li>
 *     <li>visibleRegionType: 区域范围类型，{@link com.everhomes.rest.visibility.VisibleRegionType}</li>
 *     <li>visibleRegionId: 区域范围类型对应的ID</li>
 *     <li>visibleRegionIds: 区域范围类型对应的IDs。新版的活动发布时出现了范围的概念，比如“园区A、园区C和小区A”。visibleRegionId和visibleRegionIds只传一个</li>
 *     <li>communityId: 用户当前小区ID</li>
 *     <li>subject: 公告标题</li>
 *     <li>contentType: 公告内容类型，{@link com.everhomes.rest.announcement.AnnouncementContentType}</li>
 *     <li>content: 公告内容</li>
 *     <li>embeddedAppId: 内嵌对象对应的App ID，{@link com.everhomes.rest.app.AppConstants}</li>
 *     <li>embeddedId: 内嵌对象ID</li>
 *     <li>embeddedJson: 内嵌对象列表对应的json字符串</li>
 *     <li>childCount: 孩子数目，如公告下的评论数目</li>
 *     <li>likeCount: 公告赞的数目</li>
 *     <li>dislikeCount: 公告踩的数目</li>
 *     <li>viewCount: 浏览的数目</li>
 *     <li>updateTime: 公告更新时间</li>
 *     <li>createTime: 公告创建时间</li>
 *     <li>attachments: 公告的附件信息，参见{@link com.everhomes.rest.announcement.AttachmentDTO}</li>
 *     <li>likeFlag: 是否点赞，参见{@link UserLikeType}</li>
 *     <li>favoriteFlag: 是否收藏标记，参见{@link com.everhomes.rest.announcement.AnnouncementFavoriteFlag}</li>
 *     <li>shareUrl: 分享链接</li>
 *     <li>privateFlag: 公告是否公开标记，应用场景：发给物业、政府相关部门的公告默认不公开，由物业、政府相关部门决定是否公开；参考{@link com.everhomes.rest.announcement.AnnouncementPrivacy}</li>
 *     <li>publishStatus: 公告发布状态，{@link com.everhomes.rest.announcement.AnnouncementPublishStatus}</li>
 *     <li>startTime: 开始时间</li>
 *     <li>endTime: 结束时间</li>
 *     <li>mediaDisplayFlag: 是否显示图片，0否1是</li>
 *     <li>contentUrl: 内容链接</li>
 *     <li>ownerToken: ownerToken</li>
 *     <li>interactFlag: 是否支持评论 0-no, 1-yes 参考{@link com.everhomes.rest.announcement.AnnouncementInteractFlag}</li>
 *	   <li>stickFlag: 置顶标志，0-否，1-是，参考{@link com.everhomes.rest.announcement.AnnouncementStickFlag}</li>
 *	   <li>stickTime: 置顶时间</li>
 * </ul>
 */
public class AnnouncementDTO {
    private Long id;

    private String uuid;

    private Integer namespaceId;

    private Long creatorUid;

    private String creatorNickName;

    private String creatorAvatar;

    private String creatorAvatarUrl;

    private Byte creatorAdminFlag;

    private Byte visibleRegionType;

    private Long visibleRegionId;

    @ItemType(Long.class)
    private List<Long> visibleRegionIds;

    private Long communityId;

    private String subject;

    private String contentType;

    private String content;

    private Long embeddedAppId;

    private Long embeddedId;

    // json encoded List<String> 
    private String embeddedJson;

    private Long childCount;

    private Long likeCount;

    private Long dislikeCount;

    private Long viewCount;

    private Timestamp updateTime;

    private Timestamp createTime;

    @ItemType(AttachmentDTO.class)
    private List<AttachmentDTO> attachments;

    private String forumName;

    private Byte likeFlag;

    private Byte favoriteFlag;

    private String shareUrl;

    private Byte privateFlag;

    private String publishStatus;

    private Long startTime;

    private Long endTime;

    private Byte mediaDisplayFlag;

    private String contentUrl;

    private Long groupId;

    private String groupName;

    private String ownerToken;

    private Byte interactFlag;
	
	private Byte stickFlag;

    private Timestamp stickTime;

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

    public Long getChildCount() {
        return childCount;
    }

    public void setChildCount(Long childCount) {
        this.childCount = childCount;
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

    public String getOwnerToken() {
        return ownerToken;
    }

    public void setOwnerToken(String ownerToken) {
        this.ownerToken = ownerToken;
    }

    public List<Long> getVisibleRegionIds() {
        return visibleRegionIds;
    }

    public void setVisibleRegionIds(List<Long> visibleRegionIds) {
        this.visibleRegionIds = visibleRegionIds;
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


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
