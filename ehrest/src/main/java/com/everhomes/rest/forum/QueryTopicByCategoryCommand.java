// @formatter:off
package com.everhomes.rest.forum;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID，只支持社区论坛</li>
 * <li>entityTag: 实体标签，与帖子里的creatorTag和targetTag匹配；当需要查物业/业委/居委/公安/社区工作站的帖时，必须指明该标签，否则默认只查普通用户的帖，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 * <li>communityId: 用户当前小区ID</li>
 * <li>contentCategory: 内容类型</li>
 * <li>actionCategory: 动作类型，对应以前的serviceType</li>
 * <li>embeddedAppId: appid，{@link com.everhomes.rest.app.AppConstants}</li>
 * <li>excludeCategories: 排除类型[{@link com.everhomes.rest.category.CategoryConstants}]</li>
 * <li>pageAnchor: 开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class QueryTopicByCategoryCommand {
    private Long forumId;
    private String entityTag;
    private Long communityId;
    private Long contentCategory;
    private Long actionCategory;
    private Long pageAnchor;
    private Integer pageSize;
    private Long embeddedAppId;  
    
    @ItemType(Long.class)
    private List<Long> excludeCategories;
    
    public QueryTopicByCategoryCommand() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public String getEntityTag() {
        return entityTag;
    }

    public void setEntityTag(String entityTag) {
        this.entityTag = entityTag;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(Long contentCategory) {
        this.contentCategory = contentCategory;
    }

    public Long getActionCategory() {
        return actionCategory;
    }

    public void setActionCategory(Long actionCategory) {
        this.actionCategory = actionCategory;
    }
	
    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    

    public List<Long> getExcludeCategories() {
		return excludeCategories;
	}

	public void setExcludeCategories(List<Long> excludeCategories) {
		this.excludeCategories = excludeCategories;
	}

	
	public Long getEmbeddedAppId() {
		return embeddedAppId;
	}

	public void setEmbeddedAppId(Long embeddedAppId) {
		this.embeddedAppId = embeddedAppId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
