package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orderId : 订单id</li>
 * <li>ownerType : 传community</li>
 * <li>ownerId : 项目id</li>
 * <li>organizationId : 用户公司id</li>
 * <li>clientAppName : app名称</li>
 * <li>paymentMerchantId : 商户id</li>
 * <li>appOriginId : 应用originId</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月6日
 */
public class PayPrintGeneralOrderCommand {
	
	private Long orderId;
	private String ownerType;
	private Long ownerId;
	private Long organizationId;
	private String clientAppName;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getClientAppName() {
		return clientAppName;
	}

	public void setClientAppName(String clientAppName) {
		this.clientAppName = clientAppName;
	}
}
