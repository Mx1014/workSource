package com.everhomes.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为postByCategory时点击item需要的参数
 * <li>forumId: 论坛id</li>
 * <li>entityTag: 查询者的标签，用户或管理员</li>
 * <li>entityId: 查询目标id，小区id或组织id</li>
 * <li>targetTag: 查询类型帖子标签类型，如物业PM,业委GARC等</li> 
 * <li>displayName: 帖子类型显示名</li> 
 * <li>contentCategory: 帖子类型如投诉建议、维修等</li>
 * <li>actionCategory: </li>
 * </ul>
 */
public class PostByCategoryActionData implements Serializable{

    private static final long serialVersionUID = 882096233068114981L;
    //{"contentCategory":1006,"actionCategory":0,"forumId":1,"entityTag":"USER","displayName":"投诉","entityId":1,"targetTag":"PM"} 
    private Long forumId;
    private String entityTag;
    private Long entityId;
    private Long targetTag;
    private String displayName;
    private Long contentCategory;
    private Long actionCategory;


    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getActionCategory() {
        return actionCategory;
    }

    public void setActionCategory(Long actionCategory) {
        this.actionCategory = actionCategory;
    }

    public Long getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(Long contentCategory) {
        this.contentCategory = contentCategory;
    }

    public String getEntityTag() {
        return entityTag;
    }

    public void setEntityTag(String entityTag) {
        this.entityTag = entityTag;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getTargetTag() {
        return targetTag;
    }

    public void setTargetTag(Long targetTag) {
        this.targetTag = targetTag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
