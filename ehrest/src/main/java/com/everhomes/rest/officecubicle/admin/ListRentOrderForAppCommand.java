package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定空间的请求参数
  * <li>namespaceId : namespaceId </li>
  * <li>ownerType : community 工位发布的范围</li>
 * <li>ownerId : communityId 范围的id</li>
 * <li>spaceName: 查询空间名</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 一页的大小</li> 
 * <li>orderStatus：订单状态{@link com.everhomes.rest.officecubicle.OfficeCubiceOrderStatus}</li>
 * <li>spaceId:空间ID</li>
 * <li>rentType:1长租，0短租</li>
 * </ul>
 */
public class ListRentOrderForAppCommand {
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private String spaceName;
	private Long spaceId;
	private Long pageAnchor;
	private Integer pageSize;
	private Byte orderStatus;
	private Byte rentType;

	
	public Long getSpaceId() {
		return spaceId;
	}


	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}



	public Integer getNamespaceId() {
		return namespaceId;
	}


	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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



	public String getSpaceName() {
		return spaceName;
	}


	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
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

	public Byte getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Byte orderStatus) {
		this.orderStatus = orderStatus;
	}



	public Byte getRentType() {
		return rentType;
	}

	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
