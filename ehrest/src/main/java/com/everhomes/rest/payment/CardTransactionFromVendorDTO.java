package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>mobile: 手机号</li>
 * <li>userName: 用户名称</li>
 * <li>cardNo: 卡号</li>
 * <li>orderNo: 订单号</li>
 * <li>comsumeType: 消费类型</li>
 * <li>itemName: 商品名称</li>
 * <li>amount: 金额</li>
 * <li>transactionTime: 交易时间</li>
 * <li>status: 状态码</li>
 * <li>nextPageAnchor: 分页的瞄 </li>
 * <li>merchant: 商户</li>
 * <li>transactionType: 交易类型   1：充值   2 ：消费</li>
 * <li>vendorName: 第三方厂商</li>
 * <li>vendorResult: </li>
 * <li>token: </li>
 * <li>cardNo: </li>
 * <li>payerUid: </li>
 * </ul>
 */
public class CardTransactionFromVendorDTO {
	
	private java.lang.Long       id;
	private java.lang.String     userName;
	private java.lang.String     mobile;
	private java.lang.String     cardNo;
	private java.lang.Long       orderNo;
	private java.lang.Byte       consumeType;
	private java.lang.String     itemName;
	private java.math.BigDecimal amount;
	private java.sql.Timestamp   transactionTime;
	private String status;
	
	private String merchant;
	private String transactionType;
	private String vendorName;
	private String vendorResult;
	
    private Long nextPageAnchor;
	
	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.String getUserName() {
		return userName;
	}

	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	public java.lang.String getMobile() {
		return mobile;
	}

	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}

	public java.lang.String getCardNo() {
		return cardNo;
	}

	public void setCardNo(java.lang.String cardNo) {
		this.cardNo = cardNo;
	}

	public java.lang.Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(java.lang.Long orderNo) {
		this.orderNo = orderNo;
	}

	public java.lang.Byte getConsumeType() {
		return consumeType;
	}

	public void setConsumeType(java.lang.Byte consumeType) {
		this.consumeType = consumeType;
	}

	public java.lang.String getItemName() {
		return itemName;
	}

	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}

	public java.math.BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}

	public java.sql.Timestamp getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(java.sql.Timestamp transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorResult() {
		return vendorResult;
	}

	public void setVendorResult(String vendorResult) {
		this.vendorResult = vendorResult;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
