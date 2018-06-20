// @formatter:off
package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 所属上级的id</li>
 * <li>ownerType :所属上级类型 0小区 1企业 2家庭{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListLocalCamerasCommand {
	@NotNull
	private Long ownerId;

	@NotNull
	private Byte ownerType;

	private String search;

	private Long pageAnchor;

	private Integer pageSize;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Byte getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(Byte ownerType) {
		this.ownerType = ownerType;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
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
