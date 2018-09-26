package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>itemName: 商品名称</li>
 * <li>amount: 金额</li>
 * <li>transactionTime: 交易时间</li>
 * <li>status: 状态码</li>
 * <li>nextPageAnchor: 分页的瞄 </li>
 * <li>merchant: 商户</li>
 * <li>transactionType: 交易类型   1：充值   2 ：消费</li>
 * <li>vendorName: 第三方厂商</li>
 * <li>paidType: 支付类型</li>
 * <li>vendorResult: </li>
 * </ul>
 */
public class CardTransactionFromVendorDTO implements Comparable<CardTransactionFromVendorDTO>{
	

	private java.lang.String     itemName;
	private java.math.BigDecimal amount;
	private Long   transactionTime;
	private String status;
	
	private String merchant;
	private String transactionType;
	private String vendorName;
	private String vendorResult;
	private String paidType;
	
    private Long nextPageAnchor;
	
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

	public Long getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(Long transactionTime) {
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

	public String getPaidType() {
		return paidType;
	}

	public void setPaidType(String paidType) {
		this.paidType = paidType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	@Override
	public int compareTo(CardTransactionFromVendorDTO o) {
		if(this.transactionTime.longValue() < o.transactionTime.longValue())
			return 1;
		if(this.transactionTime.longValue() > o.transactionTime.longValue())
			return -1;
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
        if(this == obj)
        	return true;
        if(obj instanceof CardTransactionFromVendorDTO)
        	return this.transactionTime.longValue() == ((CardTransactionFromVendorDTO)obj).transactionTime.longValue();
        return false;
    }
}
