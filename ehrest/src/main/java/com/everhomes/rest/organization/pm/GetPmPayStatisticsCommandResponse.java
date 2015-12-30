package com.everhomes.rest.organization.pm;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>年度收入 : yearIncomeAmount</li>
 * <li>待收费 : unPayAmount</li>
 * <li>欠费户数 : oweFamilyCount</li>
 * </ul>
 * 
 *
 */
public class GetPmPayStatisticsCommandResponse {
	private BigDecimal yearIncomeAmount;
	private BigDecimal unPayAmount;
	private Integer oweFamilyCount;
	
	public BigDecimal getYearIncomeAmount() {
		return yearIncomeAmount;
	}
	public void setYearIncomeAmount(BigDecimal yearIncomeAmount) {
		this.yearIncomeAmount = yearIncomeAmount;
	}
	public BigDecimal getUnPayAmount() {
		return unPayAmount;
	}
	public void setUnPayAmount(BigDecimal unPayAmount) {
		this.unPayAmount = unPayAmount;
	}
	public Integer getOweFamilyCount() {
		return oweFamilyCount;
	}
	public void setOweFamilyCount(Integer oweFamilyCount) {
		this.oweFamilyCount = oweFamilyCount;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	

}
