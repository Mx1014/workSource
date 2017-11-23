// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>ownerType : 打印所属类型, 参考{@link com.everhomes.rest.print.PrintOwnerType}</li>
 * <li>ownerId : 所属id</li>
 * <li>orderId : 订单id</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class PayPrintOrderCommand {
	private Long orderId;
	private String ownerType;
	private Long ownerId;

	public PayPrintOrderCommand() {
	}

	public PayPrintOrderCommand(Long orderId) {
		this.orderId = orderId;
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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
