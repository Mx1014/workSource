package com.everhomes.rest.statistics.transaction;

/**
 *<ul>
 *<li>communityId:小區ID</li>
 *<li>namespaceId:域id</li>
 *<li>communityName:小區名稱</li>
 *<li>serviceType:服務類型</li>
 *<li>resourceType:店鋪來源類型</li>
 *<li>resourceId:店鋪id</li>
 *<li>resourceName:店鋪名稱</li>
 *<li>alipayPaidAmount:支付寶交易總額</li>
 *<li>alipayRefundAmount:支付寶退款總額</li>
 *<li>wechatPaidAmount:微信交易總額</li>
 *<li>wechatRefundAmount:微信退款總額</li>
 *<li>paymentCardPaidAmount:一卡通交易總額</li>
 *<li>paymentCardRefundAmount:一卡通退款總額</li>
 *<li>totalPaidAmount:統計交易總額</li>
 *<li>totalRefundAmount:統計退款總額</li>
 *<li>totalPaidCount:統計交易笔数</li>
 *</ul>
 */
public class StatServiceSettlementResultDTO {
	
	private Long namespaceId;
	
	private Long communityId;
	
	private String communityName;
	
	private String serviceType;
	
	private String serviceName;
	
	private String resourceType;
	
	private String resourceId;
	
	private String resourceName;
	
	private Double alipayPaidAmount;
	
	private Double alipayRefundAmount;
	
	private Double wechatPaidAmount;
	
	private Double wechatRefundAmount;
	
	private Double paymentCardPaidAmount;
	
	private Double paymentCardRefundAmount;
	
	private Double totalPaidAmount;
	
	private Double totalRefundAmount;

	private Long totalPaidCount;

	public Long getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Long namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Double getAlipayPaidAmount() {
		return alipayPaidAmount;
	}

	public void setAlipayPaidAmount(Double alipayPaidAmount) {
		this.alipayPaidAmount = alipayPaidAmount;
	}

	public Double getAlipayRefundAmount() {
		return alipayRefundAmount;
	}

	public void setAlipayRefundAmount(Double alipayRefundAmount) {
		this.alipayRefundAmount = alipayRefundAmount;
	}

	public Double getWechatPaidAmount() {
		return wechatPaidAmount;
	}

	public void setWechatPaidAmount(Double wechatPaidAmount) {
		this.wechatPaidAmount = wechatPaidAmount;
	}

	public Double getWechatRefundAmount() {
		return wechatRefundAmount;
	}

	public void setWechatRefundAmount(Double wechatRefundAmount) {
		this.wechatRefundAmount = wechatRefundAmount;
	}

	public Double getPaymentCardPaidAmount() {
		return paymentCardPaidAmount;
	}

	public void setPaymentCardPaidAmount(Double paymentCardPaidAmount) {
		this.paymentCardPaidAmount = paymentCardPaidAmount;
	}

	public Double getPaymentCardRefundAmount() {
		return paymentCardRefundAmount;
	}

	public void setPaymentCardRefundAmount(Double paymentCardRefundAmount) {
		this.paymentCardRefundAmount = paymentCardRefundAmount;
	}

	public Double getTotalPaidAmount() {
		return totalPaidAmount;
	}

	public void setTotalPaidAmount(Double totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}

	public Double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(Double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

	public Long getTotalPaidCount() {
		return totalPaidCount;
	}

	public void setTotalPaidCount(Long totalPaidCount) {
		this.totalPaidCount = totalPaidCount;
	}
}
