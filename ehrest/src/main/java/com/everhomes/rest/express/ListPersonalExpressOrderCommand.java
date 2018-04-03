// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>status: 订单状态，参考{@link com.everhomes.rest.express.ExpressOrderStatus}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListPersonalExpressOrderCommand {

	private String ownerType;

	private Long ownerId;

	private Byte status;

	private Long pageAnchor;

	private Integer pageSize;

	public ListPersonalExpressOrderCommand() {

	}

	public ListPersonalExpressOrderCommand(String ownerType, Long ownerId, Byte status, Long pageAnchor, Integer pageSize) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.status = status;
		this.pageAnchor = pageAnchor;
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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
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
