package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 *     <li>billDayStats: {@link com.everhomes.rest.energy.BillStatDTO}</li> 
 *     <li>dayBurdenStats: 实际负担 list</li> 
 *     <li>lastYearPayableStats: 去年同期应付list</li> 
 *     <li>communityName: 物业服务中心</li> 
 *     <li>meterType: 类型</li> 
 *     <li>dateStr: 日期或年度</li> 
 *     <li>dates: 表头的日期</li> 
 * </ul>
 */
public class EnergyStatDTO {

    @ItemType(BillStatDTO.class)
    private List<BillStatDTO> billDayStats;

    @ItemType(DayStatDTO.class)
    private List<DayStatDTO> dayBurdenStats;  

    @ItemType(DayStatDTO.class)
    private List<DayStatDTO> lastYearPayableStats;  
    
    private String communityName ;

    private String meterType;
    
    private String dateStr ;

    @ItemType(DayStatDTO.class)
    private List<DayStatDTO> dates;  

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public List<BillStatDTO> getBillDayStats() {
		return billDayStats;
	}


	public void setBillDayStats(List<BillStatDTO> billDayStats) {
		this.billDayStats = billDayStats;
	}


	public List<DayStatDTO> getDayBurdenStats() {
		return dayBurdenStats;
	}


	public void setDayBurdenStats(List<DayStatDTO> dayBurdenStats) {
		this.dayBurdenStats = dayBurdenStats;
	}


	public List<DayStatDTO> getLastYearPayableStats() {
		return lastYearPayableStats;
	}


	public void setLastYearPayableStats(List<DayStatDTO> lastYearPayableStats) {
		this.lastYearPayableStats = lastYearPayableStats;
	} 


	public String getDateStr() {
		return dateStr;
	}


	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}


	public String getCommunityName() {
		return communityName;
	}


	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}


	public String getMeterType() {
		return meterType;
	}


	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}


	public List<DayStatDTO> getDates() {
		return dates;
	}


	public void setDates(List<DayStatDTO> dates) {
		this.dates = dates;
	}
}
