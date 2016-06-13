package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>enterpriseId: 企业id</li>
 * <li>keyword: 关键字  用户的姓名或部门</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListUsersWithoutVideoConfPrivilegeCommand {

	private Long enterpriseId;
	
	private String keyword;
	
	private Long pageAnchor;
	
    private Integer pageSize;

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
