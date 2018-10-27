// @formatter:off
package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 所属上级的id</li>
 * <li>ownerType: 所属上级类型 0小区 1企业 2家庭{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>serverId：关联服务器Id</li>
 * <li>doorAccessId：关联门禁Id</li>
 * <li>enterStatus:进出标识1进0出</li>
 * <li>linkStatus：联网状态1failed0success</li>
 * <li>pageAnchor: 下一页锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 *
 */
public class QueryAclinkCamerasCommand {
	@NotNull
	private Long ownerId;

	@NotNull
	private Byte ownerType;

	private Long serverId;
	private Long doorAccessId;
	private Byte enterStatus;
	private String name;
	private Byte linkStatus;
	private Long pageAnchor;
	private Integer pageSize;
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Byte getEnterStatus() {
		return enterStatus;
	}

	public void setEnterStatus(Byte enterStatus) {
		this.enterStatus = enterStatus;
	}

	public Byte getLinkStatus() {
		return linkStatus;
	}

	public void setLinkStatus(Byte linkStatus) {
		this.linkStatus = linkStatus;
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
