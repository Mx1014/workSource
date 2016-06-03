package com.everhomes.rest.payment;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>merchant: 商户</li>
 * <li>amount: 金额</li>
 * <li>transcationType: 交易类型 203：充值   101 ：消费</li>
 * <li>status: 00: 处理中
 	01：成功
 	02：失败
	03：已撤销
	04：已冲正
	05: 已退货
	06: 已调账
	07: 已部分退货
	{@link com.everhomes.rest.payment.CardTranscationStatus}</li>
 * <li>transcationTime: 交易时间</li>
 * </ul>
 */
public class CardTranscationsForAppDTO {
	private String merchant;
	private BigDecimal amount;
	private String transcationType;
	private String status;
	private Timestamp transcationTime;
	
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getTranscationType() {
		return transcationType;
	}
	public void setTranscationType(String transcationType) {
		this.transcationType = transcationType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getTranscationTime() {
		return transcationTime;
	}
	public void setTranscationTime(Timestamp transcationTime) {
		this.transcationTime = transcationTime;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
