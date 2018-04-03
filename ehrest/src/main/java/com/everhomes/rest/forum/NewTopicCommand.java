// @formatter:off
package com.everhomes.rest.forum;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>oldId: 老ID，当暂存过后提交时要传来老的Id用于删除老数据</li>
 *     <li>ownerType: ownerType</li>
 *     <li>ownerId: ownerId</li>
 *     <li>currentOrgId: currentOrgId</li>
 *     <li>forumId: 论坛ID</li>
 *     <li>creatorTag: 创建者标签，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 *     <li>targetTag: 创建者标签，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 *     <li>contentCategory: 内容类型ID，含类和子类</li>
 *     <li>actionCategory: 操作类型ID，如拼车中的“我搭车”、“我开车”</li>
 *     <li>visibleRegionType: 区域范围类型，{@link com.everhomes.rest.visibility.VisibleRegionType}</li>
 *     <li>visibleRegionId: 区域范围类型对应的ID</li>
 *     <li>visibleRegionIds: 区域范围类型对应的IDs。新版的活动发布时出现了范围的概念，比如“园区A、园区C和小区A”。visibleRegionId和visibleRegionIds只传一个</li>
 *     <li>longitude: 帖子内容涉及到的经度如活动</li>
 *     <li>latitude: 帖子内容涉及到的纬度如活动</li>
 *     <li>subject: 帖子标题</li>
 *     <li>contentType: 帖子内容类型，{@link com.everhomes.rest.forum.PostContentType}</li>
 *     <li>content: 帖子内容</li>
 *     <li>embeddedAppId: 内嵌对象对应的App ID，{@link com.everhomes.rest.app.AppConstants}</li>
 *     <li>embeddedId: 内嵌对象对应的ID</li>
 *     <li>embeddedJson: 内嵌对象列表对应的json字符串</li>
 *     <li>isForwarded: 是否是转发帖的标记</li>
 *     <li>attachments: 图片、语音、视频等附件信息，参考{@link com.everhomes.rest.forum.AttachmentDescriptor}</li>
 *     <li>privateFlag: 帖子是否公开标记，应用场景：发给物业、政府相关部门的帖子默认不公开，由物业、政府相关部门决定是否公开；参考{@link com.everhomes.rest.forum.PostPrivacy}</li>
 *     <li>startTime: 开始时间</li>
 *     <li>endTime: 结束时间</li>
 *     <li>officialFlag: 是否为官方帖；参考{@link com.everhomes.rest.organization.OfficialFlag}</li>
 *     <li>mediaDisplayFlag: 是否显示图片，0否1是</li>
 *     <li>maxQuantity: 限制人数</li>
 *     <li>status: 活动状态，0-已删除，1-待确认，2-正常。用于暂存或者立刻发布，不传默认2立刻发布，参考{@link com.everhomes.rest.forum.PostStatus}</li>
 *     <li>tag: 帖子标签</li>
 *     <li>cloneFlag: 克隆标识，参考{@link com.everhomes.rest.forum.PostCloneFlag}</li>
 *     <li>realPostId: 真身帖的Id</li>
 *     <li>namespaceId: 域空间Id</li>
 *     <li>forumEntryId: 论坛应用入口Id</li>
 *     <li>moduleType: 模块类型，现在所有的帖子都要往帖子表里写，通过判断条件已经很难区分是哪里来的帖子了，现在由创建帖子的时候带来。 参考{@link ForumModuleType}</li>
 *     <li>moduleCategoryId: 业务模块的入口id</li>
 * </ul>
 */
public class NewTopicCommand {
    private Long oldId;

    private String ownerType;

    private Long ownerId;

    private Long currentOrgId;

    @NotNull
    private Long forumId;

    private String creatorTag;

    private String targetTag;

    private Long contentCategory;

    private Long actionCategory;

    private Byte visibleRegionType;

    private Long visibleRegionId;

    @ItemType(Long.class)
    private List<Long> visibleRegionIds;

    private Double longitude;

    private Double latitude;

    @NotNull
    private String subject;

    //去掉非空验证
    private String contentType;

    //去掉非空验证
    private String content;

    private Long embeddedAppId;

    private Long embeddedId;

    // json encoded List<String> 
    private String embeddedJson;

    private Byte isForwarded;

    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;

    private Byte privateFlag;

    private Long startTime;

    private Long endTime;

    private Byte officialFlag;

    private Byte mediaDisplayFlag;

    private Integer maxQuantity;

    private Byte status;

    private String tag;

    private Byte cloneFlag;

    private Long realPostId;

    private Integer namespaceId;

    private Long forumEntryId;

    private Byte moduleType;

    private Long moduleCategoryId;

    public Long getOldId() {
        return oldId;
    }

    public void setOldId(Long oldId) {
        this.oldId = oldId;
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

    public Byte getOfficialFlag() {
        return officialFlag;
    }

    public void setOfficialFlag(Byte officialFlag) {
        this.officialFlag = officialFlag;
    }

    public NewTopicCommand() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
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

    public List<Long> getVisibleRegionIds() {
        return visibleRegionIds;
    }

    public void setVisibleRegionIds(List<Long> visibleRegionIds) {
        this.visibleRegionIds = visibleRegionIds;
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

    public List<AttachmentDescriptor> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDescriptor> attachments) {
        this.attachments = attachments;
    }

    public Byte getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(Byte privateFlag) {
        this.privateFlag = privateFlag;
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

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getCurrentOrgId() {
        return currentOrgId;
    }

    public void setCurrentOrgId(Long currentOrgId) {
        this.currentOrgId = currentOrgId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Byte getCloneFlag() {
        return cloneFlag;
    }

    public void setCloneFlag(Byte cloneFlag) {
        this.cloneFlag = cloneFlag;
    }

    public Long getRealPostId() {
        return realPostId;
    }

    public void setRealPostId(Long realPostId) {
        this.realPostId = realPostId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getForumEntryId() {
        return forumEntryId;
    }

    public void setForumEntryId(Long forumEntryId) {
        this.forumEntryId = forumEntryId;
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
