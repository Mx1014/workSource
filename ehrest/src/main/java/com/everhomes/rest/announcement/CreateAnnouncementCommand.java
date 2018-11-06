// @formatter:off
package com.everhomes.rest.announcement;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.forum.ForumModuleType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>currentOrgId: currentOrgId</li>
 *     <li>creatorTag: 创建者标签，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 *     <li>targetTag: 创建者标签，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 *     <li>visibleRegionType: 区域范围类型，{@link com.everhomes.rest.visibility.VisibleRegionType}</li>
 *     <li>visibleRegionId: 区域范围类型对应的ID</li>
 *     <li>visibleRegionIds: 区域范围类型对应的IDs。新版的活动发布时出现了范围的概念，比如“园区A、园区C和小区A”。visibleRegionId和visibleRegionIds只传一个</li>
 *     <li>subject: 帖子标题</li>
 *     <li>contentType: 帖子内容类型，{@link com.everhomes.rest.forum.PostContentType}</li>
 *     <li>content: 帖子内容</li>
 *     <li>embeddedAppId: 内嵌对象对应的App ID，{@link com.everhomes.rest.app.AppConstants}</li>
 *     <li>embeddedId: 内嵌对象对应的ID</li>
 *     <li>embeddedJson: 内嵌对象列表对应的json字符串</li>
 *     <li>attachments: 图片、语音、视频等附件信息，参考{@link AttachmentDescriptor}</li>
 *     <li>startTime: 开始时间</li>
 *     <li>endTime: 结束时间</li>
 *     <li>mediaDisplayFlag: 是否显示图片，0否1是</li>
 *     <li>namespaceId: 域空间Id</li>
 *     <li>forumEntryId: 论坛应用入口Id</li>
 *     <li>moduleType: 模块类型，现在所有的帖子都要往帖子表里写，通过判断条件已经很难区分是哪里来的帖子了，现在由创建帖子的时候带来。 参考{@link ForumModuleType}</li>
 *     <li>moduleCategoryId: 业务模块的入口id</li>
 *     <li>communityId: 项目(园区/小区)ID</li>
 *     <li>noticeUserFlag: 是否推送公告给用户,请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class CreateAnnouncementCommand {

    private Long currentOrgId;

    private String creatorTag;

    private String targetTag;

    private Byte visibleRegionType;

    private Long visibleRegionId;

    @ItemType(Long.class)
    private List<Long> visibleRegionIds;

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

    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;

    private Long startTime;

    private Long endTime;

    private Byte mediaDisplayFlag;

    private Integer namespaceId;

    private Long forumEntryId;

    private Byte moduleType;

    private Long moduleCategoryId;

    private Long communityId;

    private Byte noticeUserFlag;

    public Byte getNoticeUserFlag() {
        return noticeUserFlag;
    }

    public void setNoticeUserFlag(Byte noticeUserFlag) {
        this.noticeUserFlag = noticeUserFlag;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getMediaDisplayFlag() {
        return mediaDisplayFlag;
    }

    public void setMediaDisplayFlag(Byte mediaDisplayFlag) {
        this.mediaDisplayFlag = mediaDisplayFlag;
    }

    public CreateAnnouncementCommand() {
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

    public List<AttachmentDescriptor> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDescriptor> attachments) {
        this.attachments = attachments;
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

    public Long getCurrentOrgId() {
        return currentOrgId;
    }

    public void setCurrentOrgId(Long currentOrgId) {
        this.currentOrgId = currentOrgId;
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
