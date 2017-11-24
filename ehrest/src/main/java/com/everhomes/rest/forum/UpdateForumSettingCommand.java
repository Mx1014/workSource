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

    @ItemType(String.class)
    private List<String> serviceTypes;
    @ItemType(String.class)
    private List<String> topicTags;
    @ItemType(String.class)
    private List<String> activityTags;
    @ItemType(String.class)
    private List<String> pollTags;
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

    public List<String> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(List<String> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public List<String> getTopicTags() {
        return topicTags;
    }

    public void setTopicTags(List<String> topicTags) {
        this.topicTags = topicTags;
    }

    public List<String> getActivityTags() {
        return activityTags;
    }

    public void setActivityTags(List<String> activityTags) {
        this.activityTags = activityTags;
    }

    public List<String> getPollTags() {
        return pollTags;
    }

    public void setPollTags(List<String> pollTags) {
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
