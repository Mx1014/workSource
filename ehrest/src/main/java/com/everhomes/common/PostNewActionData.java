package com.everhomes.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为topic action时点击item需要的参数
 * <li>forumId: 论坛id</li>
 * <li>postId: 帖子id</li>
 * </ul>
 */
public class PostNewActionData implements Serializable{
    private static final long serialVersionUID = -4277718167680363828L;
//{"contentCategory":9,"actionCategory":3092,"forumId":1,"entityTag":"PM"}  
    private Long forumId;
    private Long actionCategory;
    private Long contentCategory;
    private String entityTag;
    
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
