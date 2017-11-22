// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.rest.hotTag.TagDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>forumId: forumId</li>
 *     <li>entryId: entryId</li>
 *     <li>serviceTypes: 服务类型 {@link com.everhomes.rest.forum.ForumServiceTypeDTO}</li>
 *     <li>topicTags: 话题热门标签 {@link com.everhomes.rest.hotTag.TagDTO}</li>
 *     <li>activityTags: 活动热门标签 {@link com.everhomes.rest.hotTag.TagDTO}</li>
 *     <li>pollTags: 投票热门标签 {@link com.everhomes.rest.hotTag.TagDTO}</li>
 *     <li>interactFlag: 是否支持交互 {@link com.everhomes.rest.forum.InteractFlag}</li>
 * </ul>
 */
public class UpdateForumSettingCommand {

    private Integer namespaceId;
    private Long forumId;
    private Long entryId;
    private List<ForumServiceTypeDTO> serviceTypes;
    private List<TagDTO> topicTags;
    private List<TagDTO> activityTags;
    private List<TagDTO> pollTags;
    private Byte interactFlag;


    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public List<ForumServiceTypeDTO> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(List<ForumServiceTypeDTO> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public List<TagDTO> getTopicTags() {
        return topicTags;
    }

    public void setTopicTags(List<TagDTO> topicTags) {
        this.topicTags = topicTags;
    }

    public List<TagDTO> getActivityTags() {
        return activityTags;
    }

    public void setActivityTags(List<TagDTO> activityTags) {
        this.activityTags = activityTags;
    }

    public List<TagDTO> getPollTags() {
        return pollTags;
    }

    public void setPollTags(List<TagDTO> pollTags) {
        this.pollTags = pollTags;
    }

    public Byte getInteractFlag() {
        return interactFlag;
    }

    public void setInteractFlag(Byte interactFlag) {
        this.interactFlag = interactFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
