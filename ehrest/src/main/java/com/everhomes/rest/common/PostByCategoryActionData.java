package com.everhomes.rest.common;

import java.io.Serializable;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为postByCategory时点击item需要的参数
 * <li>forumId: 论坛id</li>
 * <li>entityTag: 要查询的实体标签，填物业、业委、居委、公安等的一种</li>
 * <li>displayName: 帖子类型显示名</li> 
 * <li>contentCategory: 帖子类型如投诉建议、维修等</li>
 * <li>actionCategory: </li>
 * <li>organizationId: 组织id，管理员查帖使用，普通用户查帖该id为空</li>
 * <li>communityId: 小区id，普通用户查询使用，管理员查帖该id为空</li>
 * </ul>
 */
public class PostByCategoryActionData implements Serializable{

    private static final long serialVersionUID = 882096233068114981L;
    //{"contentCategory":1006,"actionCategory":0,"forumId":1,"entityTag":"PM","displayName":"投诉"} 
    private Long forumId;
    private String entityTag;
    private String displayName;
    private Long contentCategory;
    private Long actionCategory;
    private Long organizationId;
    private Long communityId;
    private Long embeddedAppId;
    
    @ItemType(Long.class)
    private List<Long> excludeCategories;


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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
    

    public Long getEmbeddedAppId() {
		return embeddedAppId;
	}

	public void setEmbeddedAppId(Long embeddedAppId) {
		this.embeddedAppId = embeddedAppId;
	}

	public List<Long> getExcludeCategories() {
		return excludeCategories;
	}

	public void setExcludeCategories(List<Long> excludeCategories) {
		this.excludeCategories = excludeCategories;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
