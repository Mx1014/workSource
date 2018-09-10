// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 所属上级的id</li>
 * <li>ownerType: 所属上级类型 0小区 1企业 2家庭{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListLocalIpadCommand {
	@NotNull
	private Long ownerId;

	@NotNull
	private Byte ownerType;

	private String search;
	
	private Long serverId;
	
	private List<Long> serverIds;
	
	private Long doorAccessId;
	
	private Byte enterStatus;
	
	private Byte linkStatus;
	
	private Byte activeStatus;
	
	private String uuid;
	
	private String name;
	
	private Integer count;

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

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public List<Long> getServerIds() {
		return serverIds;
	}

	public void setServerIds(List<Long> serverIds) {
		this.serverIds = serverIds;
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

	public Byte getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Byte activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize == null ? 0 : pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
