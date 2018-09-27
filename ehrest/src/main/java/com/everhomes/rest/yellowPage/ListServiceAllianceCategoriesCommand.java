package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> namespaceId: 域空间id</li>
 *  <li> parentId: 传服务联盟类型id </li>
 *  <li> ownerType: 拥有者类型：现在是community</li>
 *  <li> ownerId: 拥有者ID</li>
 *  <li> destination: 输出终端 1：客户端 2：浏览器</li>
 *  <li> pageAnchor: 锚点</li>
 *  <li> pageSize: 每页大小</li>
 * </ul>
 */
public class ListServiceAllianceCategoriesCommand {
	
	private Integer namespaceId;

	private Long parentId;
	
	private String ownerType;
	
	private Long ownerId;

	private Byte destination;

	private Long pageAnchor;

	private Integer pageSize;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getDestination() {
		return destination;
	}

	public void setDestination(Byte destination) {
		this.destination = destination;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
}
