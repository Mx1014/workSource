// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.hotTag.TagDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>moduleType: 模块类型  参考 {@link com.everhomes.rest.forum.ForumModuleType}</li>
 *     <li>categoryId: 模块入口id，活动、论坛等多入口的模块需要传</li>
 *     <li>serviceTypes: 服务类型 {@link com.everhomes.rest.forum.ForumServiceType}</li>
 *     <li>topicTags: 话题热门标签 </li>
 *     <li>activityTags: 活动热门标签</li>
 *     <li>pollTags: 投票热门标签</li>
 *     <li>interactFlag: 是否支持交互 {@link com.everhomes.rest.forum.InteractFlag}</li>
 * </ul>
 */
public class UpdateForumSettingCommand {

    private Integer namespaceId;

    private Byte moduleType;

    private Long categoryId;

    @ItemType(ForumServiceTypeDTO.class)
    private List<ForumServiceTypeDTO> serviceTypes;

    @ItemType(TagDTO.class)
    private List<TagDTO> topicTags;

    @ItemType(TagDTO.class)
    private List<TagDTO> activityTags;

    @ItemType(TagDTO.class)
    private List<TagDTO> pollTags;

    private Byte interactFlag;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getModuleType() {
        return moduleType;
    }

    public void setModuleType(Byte moduleType) {
        this.moduleType = moduleType;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
