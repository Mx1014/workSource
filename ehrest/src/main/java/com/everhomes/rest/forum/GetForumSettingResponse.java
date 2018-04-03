// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.hotTag.TagDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>serviceTypes: 服务类型 {@link ForumServiceTypeDTO}</li>
 *     <li>topicTags: 话题热门标签 {@link TagDTO}</li>
 *     <li>activityTags: 活动热门标签 {@link TagDTO}</li>
 *     <li>pollTags: 投票热门标签 {@link TagDTO}</li>
 *     <li>interactFlag: 是否支持交互 {@link InteractFlag}</li>
 * </ul>
 */
public class GetForumSettingResponse {
    @ItemType(ForumServiceTypeDTO.class)
    private List<ForumServiceTypeDTO> serviceTypes;

    @ItemType(TagDTO.class)
    private List<TagDTO> topicTags;

    @ItemType(TagDTO.class)
    private List<TagDTO> activityTags;

    @ItemType(TagDTO.class)
    private List<TagDTO> pollTags;

    private Byte interactFlag;

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
