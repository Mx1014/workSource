package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/10/17.
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * <li>communityId: 给客户端进行可见范围筛选</li>
*/
public class ListLeaseProjectsCommand {
    private Integer namespaceId;
    private Long cityId;
    private Long areaId;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;
    private Long categoryId;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;
	private Long communityId;

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
}
