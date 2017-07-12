package com.everhomes.rest.ui.user;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 *<ul>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * <li>tag:活动标签</li>
 * <li>scope: 范围，{@link com.everhomes.rest.ui.user.ActivityLocationScope}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * <li>categoryId: 活动类型id</li>
 * <li>contentCategoryId: 主题分类id</li>
 *</ul>
 */
public class ListNearbyActivitiesBySceneCommand {
    private String sceneToken;
    
    private String tag;
    
    private Byte scope;
    
    private Long pageAnchor;
    
    private Integer pageSize;
    
    private Long categoryId;
    
    private Long contentCategoryId;

    @ItemType(Integer.class)
    private List<Integer> activityStatusList;

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

	public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
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

    public List<Integer> getActivityStatusList() {
        return activityStatusList;
    }

    public void setActivityStatusList(List<Integer> activityStatusList) {
        this.activityStatusList = activityStatusList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
