package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 *     <li>billDayStats: {@link com.everhomes.rest.energy.BillStatDTO}</li> 
 *     <li>dayBurdenStats: 实际负担 list</li> 
 * </ul>
 */
public class EnergyStatDTO {

    @ItemType(BillStatDTO.class)
    private List<BillStatDTO> billDayStats;

    @ItemType(DayStatDTO.class)
    private List<DayStatDTO> dayBurdenStats;  
  
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
}
