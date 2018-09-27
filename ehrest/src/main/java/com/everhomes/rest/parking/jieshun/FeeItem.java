package com.everhomes.rest.parking.jieshun;

import com.everhomes.util.StringHelper;

public class FeeItem {
	
	private Integer monthPeriod;
	private String money;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Integer getMonthPeriod() {
		return monthPeriod;
	}

	public void setMonthPeriod(Integer monthPeriod) {
		this.monthPeriod = monthPeriod;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
}
