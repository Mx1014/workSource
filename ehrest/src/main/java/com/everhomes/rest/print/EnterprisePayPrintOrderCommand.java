package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType : 传"community"</li>
 * <li>ownerId : 项目id</li>
 * <li>organizationId : 公司id</li>
 * <li>orderId : 支付订单id</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月19日
 */
public class EnterprisePayPrintOrderCommand {
	private String ownerType;
	private Long ownerId;
	private Long organizationId;
	private Long orderId;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}


}
