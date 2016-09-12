package com.everhomes.rest.forum;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>queryString: 关键字</li>
 * <li>forumIds: 论坛id列表</li>
 * <li>communityIds: 小区id列表</li>
 * <li>regionIds: 片区id列表</li>
 * <li>namespaceId: 域空间</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class SearchByMultiForumAndCmntyCommand {
	
	private String queryString;
    
	@ItemType(Long.class)
	private List<Long> forumIds;
    
	@ItemType(Long.class)
    private List<Long> communityIds;

    @ItemType(Long.class)
	private List<Long> regionIds;
    
    private Integer namespaceId;

    private Long pageAnchor;
    
    private Integer pageSize;
    
    private String searchContentType;

    public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public List<Long> getForumIds() {
		return forumIds;
	}

	public void setForumIds(List<Long> forumIds) {
		this.forumIds = forumIds;
	}

	public List<Long> getCommunityIds() {
		return communityIds;
	}

	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
	}

	public List<Long> getRegionIds() {
        return regionIds;
    }

    public void setRegionIds(List<Long> regionIds) {
        this.regionIds = regionIds;
    }

    public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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

	public String getSearchContentType() {
		return searchContentType;
	}

	public void setSearchContentType(String searchContentType) {
		this.searchContentType = searchContentType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
