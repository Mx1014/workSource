package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 *<ul>
 * <li>organizationId: 机构/公司ID</li>
 * <li>tag:活动标签</li>
 * <li>scope: 范围，{@link com.everhomes.rest.ui.user.ActivityLocationScope}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * <li>categoryId: 活动类型id</li>
 *</ul>
 */
public class ListOrgNearbyActivitiesCommand {
    private Long organizationId;
    
    private String tag;
    
    private Byte scope;
    
    private Long pageAnchor;
    
    private Integer pageSize;
    
    private Long categoryId;

    private Long contentCategoryId;

    @ItemType(Integer.class)
    private List<Integer> activityStatusList;

    private String sceneToken;

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

	public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Byte getScope() {
        return scope;
    }

    public void setScope(Byte scope) {
        this.scope = scope;
    }

    public List<Integer> getActivityStatusList() {
        return activityStatusList;
    }

    public void setActivityStatusList(List<Integer> activityStatusList) {
        this.activityStatusList = activityStatusList;
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

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
