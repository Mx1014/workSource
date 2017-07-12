package com.everhomes.rest.energy;

import java.io.Serializable;
import java.math.BigDecimal;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 *     <li>statDate: 日期时间戳</li>
 *     <li>dateStr: 日期字符串</li>
 *     <li>lastReading: 上次读数</li>
 *     <li>currentReading: 本次读数</li>
 *     <li>currentAmount: 本次用量</li>
 *     <li>currentCost: 本次费用</li>
 * </ul>
 */
public class DayStatDTO implements Serializable {
 
	private Long statDate;
	private String dateStr;
    private BigDecimal lastReading;
    private BigDecimal currentReading;
    private BigDecimal currentAmount;
    private BigDecimal currentCost;
    
    public DayStatDTO(){
    	this.lastReading = new BigDecimal(0);
    	this.currentReading = new BigDecimal(0);
    	this.currentAmount = new BigDecimal(0);
    	this.currentCost = new BigDecimal(0);
    }
	public Long getStatDate() {
		return statDate;
	}


	public void setStatDate(Long statDate) {
		this.statDate = statDate;
	}


	public BigDecimal getLastReading() {
		return lastReading;
	}


	public void setLastReading(BigDecimal lastReading) {
		this.lastReading = lastReading;
	}


	public BigDecimal getCurrentReading() {
		return currentReading;
	}


	public void setCurrentReading(BigDecimal currentReading) {
		this.currentReading = currentReading;
	}


	public BigDecimal getCurrentAmount() {
		return currentAmount;
	}


	public void setCurrentAmount(BigDecimal currentAmount) {
		this.currentAmount = currentAmount;
	}


	public BigDecimal getCurrentCost() {
		return currentCost;
	}


	public void setCurrentCost(BigDecimal currentCost) {
		this.currentCost = currentCost;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
 
}
