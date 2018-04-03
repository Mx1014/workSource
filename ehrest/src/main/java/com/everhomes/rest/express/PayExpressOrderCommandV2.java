// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>id: 快递id</li>
 * <li>clientAppName: Realm值，app客户端必传</li>
 * <li>paymentType: 支付方式，微信公众号支付方式必填，9-公众号支付 参考{@link com.everhomes.rest.order.Payment}</li>
 * </ul>
 */
public class PayExpressOrderCommandV2 {

	private String ownerType;

	private Long ownerId;

	private Long id;

	private String clientAppName;

	private Integer paymentType;


	public PayExpressOrderCommandV2() {

	}

	public PayExpressOrderCommandV2(String ownerType, Long ownerId, Long id) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.id = id;
	}

	public PayExpressOrderCommandV2(String ownerType, Long ownerId, Long id, String clientAppName, Integer paymentType) {
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.id = id;
		this.clientAppName = clientAppName;
		this.paymentType = paymentType;
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

	public String getClientAppName() {
		return clientAppName;
	}

	public void setClientAppName(String clientAppName) {
		this.clientAppName = clientAppName;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
