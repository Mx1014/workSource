// @formatter:off
package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>ownerId: 所属上级的id</li>
 * <li>ownerType :所属上级类型 0小区 1企业 2家庭{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>serverId：服务器id</li>
 * <li>doorAccessId：门禁Id</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class QueryServerRelationsCommand {
	@NotNull
	private Long ownerId;

	@NotNull
	private Byte ownerType;
	
	private Long serverId;
	
	private Long doorAccessId;
	
	private Long pageAnchor;

	private Integer pageSize;

	
	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public Long getDoorAccessId() {
		return doorAccessId;
	}

	public void setDoorAccessId(Long doorAccessId) {
		this.doorAccessId = doorAccessId;
	}

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

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize == null? 0 : pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
