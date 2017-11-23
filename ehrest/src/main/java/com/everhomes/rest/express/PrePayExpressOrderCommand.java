// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwner}</li>
 * <li>ownerId: 所属者id</li>
 * <li>id: 快递id</li>
 * <li>clientPayType: 客户端支付方式, 参考 {@link com.everhomes.rest.express.ExpressClientPayType}</li>
 * </ul>
 */
public class PrePayExpressOrderCommand {

	private String ownerType;

	private Long ownerId;

	private Long id;
	
	private String clientPayType;

	public PrePayExpressOrderCommand() {

	}

	public PrePayExpressOrderCommand(String ownerType, Long ownerId, Long id, String clientPayType) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.id = id;
		this.clientPayType = clientPayType;
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

	public String getClientPayType() {
		return clientPayType;
	}

	public void setClientPayType(String clientPayType) {
		this.clientPayType = clientPayType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
