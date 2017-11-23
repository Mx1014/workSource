// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>id: 订单id</li>
 * <li>statusDesc: 取消原因</li>
 * </ul>
 */
public class CancelExpressOrderCommand {

	private String ownerType;

	private Long ownerId;

	private Long id;
	
	private String statusDesc;

	public CancelExpressOrderCommand() {

	}


	public CancelExpressOrderCommand(String ownerType, Long ownerId, Long id, String statusDesc) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.id = id;
		this.statusDesc = statusDesc;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
