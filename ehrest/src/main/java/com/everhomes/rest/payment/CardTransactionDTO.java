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
 * <li>transcationTime: 交易时间</li>
 * <li>status: 状态码</li>
 * <li>nextPageAnchor: 分页的瞄 </li>
 * <li>merchant: 商户</li>
 * <li>transcationType: 交易类型   1：充值   2 ：消费</li>
 * <li>vendorName: 第三方厂商</li>
 * <li>vendorResult: </li>
 * </ul>
 */
public class CardTransactionDTO {
	private java.lang.Long       id;
	private java.lang.String     userName;
	private java.lang.String     mobile;
	private java.lang.String     cardNo;
	private java.lang.Long       orderNo;
	private java.lang.Byte       consumeType;
	private java.lang.String     itemName;
	private java.math.BigDecimal amount;
	private java.sql.Timestamp   transcationTime;
	private java.lang.Byte       status;
	
	private String merchant;
	private String transcationType;
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
	public java.sql.Timestamp getTranscationTime() {
		return transcationTime;
	}
	public void setTranscationTime(java.sql.Timestamp transcationTime) {
		this.transcationTime = transcationTime;
	}
	public java.lang.Byte getStatus() {
		return status;
	}
	public void setStatus(java.lang.Byte status) {
		this.status = status;
	}
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
    
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	public String getTranscationType() {
		return transcationType;
	}
	public void setTranscationType(String transcationType) {
		this.transcationType = transcationType;
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
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
