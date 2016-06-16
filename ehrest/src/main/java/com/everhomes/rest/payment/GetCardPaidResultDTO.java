package com.everhomes.rest.payment;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>merchantNo: 商户号</li>
 * <li>merchantName: 商户名称</li>
 * <li>amount: 金额</li>
 * <li>disAmount: 折扣金额</li>
 * <li>status: 1： 成功   0：失败 {@link com.everhomes.rest.payment.PaidResultStatus}</li>
 * <li>transactionTime: 交易时间</li>
 * </ul>
 */
public class GetCardPaidResultDTO {
	private String token;
	private String merchantNo;
	private String merchantName;
	private BigDecimal amount;
	private BigDecimal disAmount;
	private String status;
	private Timestamp transactionTime;
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getDisAmount() {
		return disAmount;
	}
	public void setDisAmount(BigDecimal disAmount) {
		this.disAmount = disAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Timestamp getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(Timestamp transactionTime) {
		this.transactionTime = transactionTime;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
