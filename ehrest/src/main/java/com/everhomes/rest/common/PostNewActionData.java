package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为topic action时点击item需要的参数
 * <li>forumId: 论坛id</li>
 * <li>actionCategory: 帖子类型，大类</li>
 * <li>contentCategory: 帖子类型，小类，如投诉建议</li>
 * <li>displayName: 显示名</li>
 * <li>creatorEntityTag: 创建者标签</li>
 * <li>targetEntityTag: 接受者标签</li>
 * <li>regionType: 可见范围类型</li>
 * </ul>
 */
public class PostNewActionData implements Serializable{
    private static final long serialVersionUID = -4277718167680363828L;
//{"contentCategory":9,"actionCategory":3092,"forumId":1,"entityTag":"PM"}  
    private Long forumId;
    private Long actionCategory;
    private Long contentCategory;
    private String entityTag;
    private String displayName;
    private String creatorEntityTag;
    private String targetEntityTag;
    private Byte visibleRegionType;
    private Long visibleRegionId;
    private Long embedAppId; 
    
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

    public String getCreatorEntityTag() {
        return creatorEntityTag;
    }

    public void setCreatorEntityTag(String creatorEntityTag) {
        this.creatorEntityTag = creatorEntityTag;
    }

    public String getTargetEntityTag() {
        return targetEntityTag;
    }

    public void setTargetEntityTag(String targetEntityTag) {
        this.targetEntityTag = targetEntityTag;
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

    public Long getEmbedAppId() {
        return embedAppId;
    }

    public void setEmbedAppId(Long embedAppId) {
        this.embedAppId = embedAppId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
