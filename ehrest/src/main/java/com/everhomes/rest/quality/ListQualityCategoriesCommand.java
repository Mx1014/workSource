package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 标准类型所属组织等的id</li>
 *  <li>ownerType: 标准类型所属组织类型，如enterprise</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 * </ul>
 */
public class ListQualityCategoriesCommand {
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private Long pageAnchor;
	
	private Integer pageSize;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
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
