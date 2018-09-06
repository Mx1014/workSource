package com.everhomes.rest.contract;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class DurationParamDTO {
	
	private String startTime;
	private String endTimeByPeriod;
	private String endTimeByDay;
	
	@ItemType(ChargingItemVariables.class)
    private List<ChargingItemVariables> chargingPaymentTypeVariables;
	
    public List<ChargingItemVariables> getChargingPaymentTypeVariables() {
		return chargingPaymentTypeVariables;
	}

	public void setChargingPaymentTypeVariables(List<ChargingItemVariables> chargingPaymentTypeVariables) {
		this.chargingPaymentTypeVariables = chargingPaymentTypeVariables;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTimeByPeriod() {
		return endTimeByPeriod;
	}

	public void setEndTimeByPeriod(String endTimeByPeriod) {
		this.endTimeByPeriod = endTimeByPeriod;
	}

	public String getEndTimeByDay() {
		return endTimeByDay;
	}

	public void setEndTimeByDay(String endTimeByDay) {
		this.endTimeByDay = endTimeByDay;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
