package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>mobile: 手机号</li>
 * <li>userName: 用户名称</li>
 * <li>cardNo: 卡号</li>
 * <li>orderNo: 订单号</li>
 * <li>consumeType: 消费类型</li>
 * <li>itemName: 商品名称</li>
 * <li>amount: 金额</li>
 * <li>transactionTime: 交易时间</li>
 * <li>status: 状态码</li>
 * <li>nextPageAnchor: 分页的瞄 </li>
 * <li>merchantNo: 商户号</li>
 * <li>merchantName: 商户名称</li>
 * <li>consumeType: 交易类型   1：充值   2 ：消费</li>
 * <li>vendorName: 第三方厂商</li>
 * <li>vendorResult: </li>
 * <li>token: </li>
 * <li>cardNo: </li>
 * <li>payerUid: </li>
 * </ul>
 */
public class CardTransactionDTO {
	
    private java.lang.Long       id;
	private java.lang.Long       payerUid;
	private java.lang.String     userName;
	private java.lang.String     mobile;
	private java.lang.String     itemName;
	private java.lang.String     merchant;
	private java.math.BigDecimal amount;
	private java.lang.String     transactionNo;
	private java.sql.Timestamp   transactionTime;
	private java.lang.Long       cardId;
	private java.lang.Byte       status;
	private java.lang.Long       creatorUid;
	private java.sql.Timestamp   createTime;
	private java.lang.String     vendorName;
	private java.lang.String     vendorResult;
	private java.lang.Byte       comsumeType;
	private java.lang.String     token;
	private java.lang.String     cardNo;
	private java.lang.Long       orderNo;
	private Long nextPageAnchor;
	
	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.Long getPayerUid() {
		return payerUid;
	}

	public void setPayerUid(java.lang.Long payerUid) {
		this.payerUid = payerUid;
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

	public java.lang.String getItemName() {
		return itemName;
	}

	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}

	public java.lang.String getMerchant() {
		return merchant;
	}

	public void setMerchant(java.lang.String merchant) {
		this.merchant = merchant;
	}

	public java.math.BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}

	public java.lang.String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(java.lang.String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public java.sql.Timestamp getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(java.sql.Timestamp transactionTime) {
		this.transactionTime = transactionTime;
	}

	public java.lang.Long getCardId() {
		return cardId;
	}

	public void setCardId(java.lang.Long cardId) {
		this.cardId = cardId;
	}

	public java.lang.Byte getStatus() {
		return status;
	}

	public void setStatus(java.lang.Byte status) {
		this.status = status;
	}

	public java.lang.Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(java.lang.Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public java.lang.String getVendorName() {
		return vendorName;
	}

	public void setVendorName(java.lang.String vendorName) {
		this.vendorName = vendorName;
	}

	public java.lang.String getVendorResult() {
		return vendorResult;
	}

	public void setVendorResult(java.lang.String vendorResult) {
		this.vendorResult = vendorResult;
	}

	public java.lang.Byte getComsumeType() {
		return comsumeType;
	}

	public void setComsumeType(java.lang.Byte comsumeType) {
		this.comsumeType = comsumeType;
	}

	public java.lang.String getToken() {
		return token;
	}

	public void setToken(java.lang.String token) {
		this.token = token;
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
