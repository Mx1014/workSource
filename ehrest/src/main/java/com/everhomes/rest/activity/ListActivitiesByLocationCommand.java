package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <p>按经纬度位置列活动</p>
 * <ul>
 * <li>namespaceId: 域空间ID</li>
 * <li>locationPointList: 经纬度列表</li>
 * <li>scope: 范围，{@link com.everhomes.rest.ui.user.ActivityLocationScope}</li>
 * <li>tag: 活动标签</li>
 * <li>officialFlag:是否是官方活动</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * <li>categoryId: 活动类型id</li>
 * </ul>
 */
public class ListActivitiesByLocationCommand {
    private Integer namespaceId;
    
    @ItemType(GeoLocation.class)
    private List<GeoLocation> locationPointList;
    
    private Byte scope;
    
    private String tag;

    private Byte officialFlag;

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

	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<GeoLocation> getLocationPointList() {
        return locationPointList;
    }

    public void setLocationPointList(List<GeoLocation> locationPointList) {
        this.locationPointList = locationPointList;
    }

    public Byte getScope() {
        return scope;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setScope(Byte scope) {
        this.scope = scope;
    }

    public Byte getOfficialFlag() {
        return officialFlag;
    }

    public void setOfficialFlag(Byte officialFlag) {
        this.officialFlag = officialFlag;
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
