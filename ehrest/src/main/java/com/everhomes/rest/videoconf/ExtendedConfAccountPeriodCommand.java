package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>accountIds: 账号id列表</li>
 *  <li>months: 延期月数</li>
 *  <li>amount: 付款金额</li>
 * </ul>
 *
 */
public class ExtendedConfAccountPeriodCommand {
	
	private List<Long> accountIds;
	
	private int months;
	
	private Double amount;

	public List<Long> getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(List<Long> accountIds) {
		this.accountIds = accountIds;
	}

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
