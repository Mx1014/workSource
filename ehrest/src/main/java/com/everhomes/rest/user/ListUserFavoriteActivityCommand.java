package com.everhomes.rest.user;


import com.everhomes.util.StringHelper;


/**
 * <p>用户所收藏过的活动：</p>
 * <ul>
 * <li>communityId: 用户所在小区或园区ID</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListUserFavoriteActivityCommand {
	private Long communityId;

	private Long pageAnchor;
    
    private Integer pageSize;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
