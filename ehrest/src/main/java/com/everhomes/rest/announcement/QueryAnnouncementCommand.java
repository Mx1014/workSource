// @formatter:off
package com.everhomes.rest.announcement;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.NeedTemporaryType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 机构ID</li>
 * <li>communityId: 小区ID</li>
 * <li>forumId: 论坛ID</li>
 * <li>contentCategory: 内容类型ID，{@link com.everhomes.rest.category.CategoryConstants}</li>
 * <li>actionCategory: 动作类型ID，对应以前的serviceType</li>
 * <li>publishStatus: 帖子发布状态，{@link com.everhomes.rest.forum.TopicPublishStatus}</li>
 * <li>embeddedAppId: appid，{@link com.everhomes.rest.app.AppConstants}</li>
 * <li>excludeCategories: 不查询的内容类型 {@link com.everhomes.rest.category.CategoryConstants}</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * <li>officialFlag: 是否为官方帖；参考{@link com.everhomes.rest.organization.OfficialFlag}</li>
 * <li>categoryId: 活动类型id</li>
 * <li>contentCategoryId: 活动主题分类id</li>
 * <li>needTemporary: 0-已发布， 1-全部，2-仅仅暂存，不填默认0 参考{@link NeedTemporaryType}</li>
 * <li>tag: 标签</li>
 * <li>namespaceId: 域空间id</li>
 * <li>forumEntryId: 论坛应用入口Id</li>
 * </ul>
 */
public class QueryAnnouncementCommand {
    private Long organizationId;
    private Long communityId;
    private Long forumId;
    private Long contentCategory;
    private Long actionCategory;
    private String publishStatus;
    private Long pageAnchor;
    private Integer pageSize;
    private Long embeddedAppId;
    private Byte officialFlag;
    private Byte privateFlag;
    private Long categoryId;
    private Byte orderByCreateTime;
    private Integer namespaceId;

    @ItemType(Long.class)
    private List<Long> excludeCategories;

    private Long contentCategoryId;

    @ItemType(Integer.class)
    private List<Integer> activityStatusList;

    private Byte needTemporary;

    private String tag;

    private Long forumEntryId;

    public QueryAnnouncementCommand() {
    }

    public Long getContentCategoryId() {
		return contentCategoryId;
	}

	public void setContentCategoryId(Long contentCategoryId) {
		this.contentCategoryId = contentCategoryId;
	}

	public Long getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}


	public Byte getOfficialFlag() {
		return officialFlag;
	}

	public void setOfficialFlag(Byte officialFlag) {
		this.officialFlag = officialFlag;
	}


    public Long getCommunityId() {
		return communityId;
	}


    public List<Integer> getActivityStatusList() {
        return activityStatusList;
    }

    public void setActivityStatusList(List<Integer> activityStatusList) {
        this.activityStatusList = activityStatusList;
    }

    public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

    public Byte getOrderByCreateTime() {
        return orderByCreateTime;
    }

    public void setOrderByCreateTime(Byte orderByCreateTime) {
        this.orderByCreateTime = orderByCreateTime;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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
    
    

    public Long getForumId() {
		return forumId;
	}



	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

	


	public String getPublishStatus() {
		return publishStatus;
	}



	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
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



	public Byte getPrivateFlag() {
		return privateFlag;
	}


	public void setPrivateFlag(Byte privateFlag) {
		this.privateFlag = privateFlag;
	}

	public Byte getNeedTemporary() {
		return needTemporary;
	}

	public void setNeedTemporary(Byte needTemporary) {
		this.needTemporary = needTemporary;
	}

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getForumEntryId() {
        return forumEntryId;
    }

    public void setForumEntryId(Long forumEntryId) {
        this.forumEntryId = forumEntryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
