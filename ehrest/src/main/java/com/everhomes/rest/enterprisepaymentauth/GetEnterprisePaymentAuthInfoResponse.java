// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import java.math.BigDecimal;


import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>currentMonthRemainAmount: 本月余额 </li>
 * <li>limitAmount: 每月总额 </li>
 * <li>historicalTotalPayAmount: 历史累计支付金额</li>
 * <li>historicalPayCount: 历史累计支付笔数</li>
 * </ul>
 */
public class GetEnterprisePaymentAuthInfoResponse {
	private BigDecimal currentMonthRemainAmount;
	private BigDecimal limitAmount;
	private BigDecimal historicalTotalPayAmount;
	private Integer historicalPayCount;

	public GetEnterprisePaymentAuthInfoResponse() {

	}

	public GetEnterprisePaymentAuthInfoResponse(BigDecimal currentMonthRemainAmount, BigDecimal limitAmount, BigDecimal historicalTotalPayAmount, Integer historicalPayCount) {
		this.currentMonthRemainAmount = currentMonthRemainAmount;
		this.limitAmount = limitAmount;
		this.historicalTotalPayAmount = historicalTotalPayAmount;
		this.historicalPayCount = historicalPayCount;
	}

	public BigDecimal getCurrentMonthRemainAmount() {
		return currentMonthRemainAmount;
	}

	public void setCurrentMonthRemainAmount(BigDecimal currentMonthRemainAmount) {
		this.currentMonthRemainAmount = currentMonthRemainAmount;
	}

	public BigDecimal getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(BigDecimal limitAmount) {
		this.limitAmount = limitAmount;
	}

	public BigDecimal getHistoricalTotalPayAmount() {
		return historicalTotalPayAmount;
	}

	public void setHistoricalTotalPayAmount(BigDecimal historicalTotalPayAmount) {
		this.historicalTotalPayAmount = historicalTotalPayAmount;
	}

	public Integer getHistoricalPayCount() {
		return historicalPayCount;
	}

	public void setHistoricalPayCount(Integer historicalPayCount) {
		this.historicalPayCount = historicalPayCount;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
