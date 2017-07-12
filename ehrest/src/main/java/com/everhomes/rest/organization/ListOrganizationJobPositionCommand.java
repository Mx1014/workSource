// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>ownerType: 所属实体类型</li>
 * <li>ownerId: 所属实体id</li>
 * <li>keywords: 关键字搜索</li>
 * <li>pageAnchor: 瞄</li>
 * <li>pageSize: 每页条数</li>
 * </ul>
 */
public class ListOrganizationJobPositionCommand {

    private String ownerType;

    private Long ownerId;

    private String keywords;
    
    private Long pageAnchor;
    
    private Integer pageSize;


    public ListOrganizationJobPositionCommand() {
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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
