// @formatter:off
package com.everhomes.rest.print;

import java.util.Map;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>ownerType : 打印所属类型, 参考{@link PrintOwnerType}</li>
 * <li>ownerId : 所属id</li>
 * <li>orderId : 订单id</li>
 * <li>clientAppName: Realm值，app客户端必传</li>
 * <li>paymentType: 支付方式，微信公众号支付方式必填，9-公众号支付 参考{@link com.everhomes.rest.order.Payment}</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class PayPrintOrderCommandV2 {
	private Long orderId;
	private String ownerType;
	private Long ownerId;
	private String clientAppName;
	private Integer namespaceId;
	private Map<String, String> paymentParams;
	private Integer commitFlag;
	
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	private Integer paymentType;
	public PayPrintOrderCommandV2() {
	}

	public PayPrintOrderCommandV2(Long orderId) {
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

	public Map<String, String> getPaymentParams() {
		return paymentParams;
	}

	public void setPaymentParams(Map<String, String> paymentParams) {
		this.paymentParams = paymentParams;
	}

	public Integer getCommitFlag() {
		return commitFlag;
	}

	public void setCommitFlag(Integer commitFlag) {
		this.commitFlag = commitFlag;
	}
}
