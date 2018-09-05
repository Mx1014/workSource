//@formatter:off
package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>chargingItemName:收费项目</li>
 * <li>startTime:开始时间</li>
 * <li>endTimeByPeriod:结束时间按周期</li>
 * <li>endTimeByDay:结束时间按天</li>
 *</ul>
 */
public class ChargingItemVariables {
	private String chargingItemName;
	private String startTime;
	private String endTimeByPeriod;
	private String endTimeByDay;

    public String getChargingItemName() {
		return chargingItemName;
	}

	public void setChargingItemName(String chargingItemName) {
		this.chargingItemName = chargingItemName;
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

	public ChargingItemVariables() {

    }
}
