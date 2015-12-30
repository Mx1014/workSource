package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为MAKERZONE(32)，创客空间
 * <li>type：类型，参考{@link com.everhomes.rest.yellowPage.YellowPageType}</li>
 * <li>forumId：论坛ID</li>
 * <li>categoryId：类型，参考{@link com.everhomes.rest.category.CategoryConstants}</li>
 * </ul>
 */
public class MarkerZoneActionData implements Serializable{
    private static final long serialVersionUID = -742724365939053762L;
    
    private Byte type;
    
    private Long forumId;
    
    private Long categoryId;

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
