// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>category: 类型，1寄件人地址，2收件人地址，参考{@link com.everhomes.rest.express.ExpressAddressCategory}</li>
 * </ul>
 */
public class ListExpressAddressCommand {

	private String ownerType;

	private Long ownerId;

	private Byte category;

	public ListExpressAddressCommand() {

	}

	public ListExpressAddressCommand(String ownerType, Long ownerId, Byte category) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.category = category;
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

	public Byte getCategory() {
		return category;
	}

	public void setCategory(Byte category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
