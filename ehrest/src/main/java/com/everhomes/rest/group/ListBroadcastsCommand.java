// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者，参考{@link com.everhomes.rest.group.BroadcastOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListBroadcastsCommand {

	private String ownerType;

	private Long ownerId;
	
	private Long pageAnchor;
	
	private Integer pageSize;

	public ListBroadcastsCommand() {

	}

	public ListBroadcastsCommand(String ownerType, Long ownerId, Long pageAnchor, Integer pageSize) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.pageAnchor = pageAnchor;
		this.pageSize = pageSize;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
