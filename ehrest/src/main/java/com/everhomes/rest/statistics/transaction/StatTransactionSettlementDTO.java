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
 *<li>totalCardPaidAmount:統計交易總額</li>
 *<li>totalCardRefundAmount:統計退款總額</li>
 *</ul>
 */
public class StatTransactionSettlementDTO {
	
	private Long namespaceId;
	
	private Long communityId;
	
	private Long communityName;
	
	private String serviceType;
	
	private String resourceType;
	
	private Long resourceId;
	
	private Long resourceName;
	
	private Double alipayPaidAmount;
	
	private Double alipayRefundAmount;
	
	private Double wechatPaidAmount;
	
	private Double wechatRefundAmount;
	
	private Double paymentCardPaidAmount;
	
	private Double paymentCardRefundAmount;
	
	private Double totalCardPaidAmount;
	
	private Double totalCardRefundAmount;

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

	public Long getCommunityName() {
		return communityName;
	}

	public void setCommunityName(Long communityName) {
		this.communityName = communityName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public Long getResourceName() {
		return resourceName;
	}

	public void setResourceName(Long resourceName) {
		this.resourceName = resourceName;
	}

	public Double getalipayPaidAmount() {
		return alipayPaidAmount;
	}

	public void setalipayPaidAmount(Double alipayPaidAmount) {
		this.alipayPaidAmount = alipayPaidAmount;
	}

	public Double getalipayRefundAmount() {
		return alipayRefundAmount;
	}

	public void setalipayRefundAmount(Double alipayRefundAmount) {
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

	public Double getpaymentCardPaidAmount() {
		return paymentCardPaidAmount;
	}

	public void setpaymentCardPaidAmount(Double paymentCardPaidAmount) {
		this.paymentCardPaidAmount = paymentCardPaidAmount;
	}

	public Double getpaymentCardRefundAmount() {
		return paymentCardRefundAmount;
	}

	public void setpaymentCardRefundAmount(Double paymentCardRefundAmount) {
		this.paymentCardRefundAmount = paymentCardRefundAmount;
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

	public Double getTotalCardPaidAmount() {
		return totalCardPaidAmount;
	}

	public void setTotalCardPaidAmount(Double totalCardPaidAmount) {
		this.totalCardPaidAmount = totalCardPaidAmount;
	}

	public Double getTotalCardRefundAmount() {
		return totalCardRefundAmount;
	}

	public void setTotalCardRefundAmount(Double totalCardRefundAmount) {
		this.totalCardRefundAmount = totalCardRefundAmount;
	}

	
}
