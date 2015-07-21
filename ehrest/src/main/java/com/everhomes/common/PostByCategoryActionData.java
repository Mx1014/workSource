package com.everhomes.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为postByCategory时点击item需要的参数
 * <li>forumId: 论坛id</li>
 * <li>contentCategory: 发帖动作的大类，如物业，家政等</li>
 * <li>actionCategory: 发帖动作的小类，如投诉，建议等</li>
 * <li>entityTag: 帖子标签</li>
 * <li>displayName: 显示名</li> 
 * <li>regionType: 可见范围类型</li>
 * <li>regionId: 范围id，小区或片区id</li> 
 * </ul>
 */
public class PostByCategoryActionData implements Serializable{

    private static final long serialVersionUID = 882096233068114981L;
    //{"contentCategory":9,"actionCategory":3092,"forumId":1,"entityTag":"PM","displayName":"投诉"} 
    private Long forumId;
    private Long actionCategory;
    private Long contentCategory;
    private String entityTag;
    private String displayName;
    private Byte regionType;
    private Long regionId;


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

    public Byte getRegionType() {
        return regionType;
    }

    public void setRegionType(Byte regionType) {
        this.regionType = regionType;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
