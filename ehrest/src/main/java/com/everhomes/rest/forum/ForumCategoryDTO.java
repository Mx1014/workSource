// @formatter:off
package com.everhomes.rest.forum;


import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>uuid: uuid</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>forumId: forumId</li>
 *     <li>entryId: entryId</li>
 *     <li>name: name</li>
 *     <li>activityEntryId: activityEntryId</li>
 *     <li>interactFlag: 是否支持评论 0-no, 1-yes 参考{@link InteractFlag}</li>
 *     <li>createTime: createTime</li>
 *     <li>updateTime: updateTime</li>
 * </ul>
 */
public class ForumCategoryDTO {

    private Long id;
    private String uuid;
    private Integer namespaceId;
    private Long forumId;
    private Long entryId;
    private String name;
    private Long activityEntryId;
    private Byte interactFlag;
    private Timestamp createTime;
    private Timestamp updateTime;

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

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getActivityEntryId() {
        return activityEntryId;
    }

    public void setActivityEntryId(Long activityEntryId) {
        this.activityEntryId = activityEntryId;
    }

    public Byte getInteractFlag() {
        return interactFlag;
    }

    public void setInteractFlag(Byte interactFlag) {
        this.interactFlag = interactFlag;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
