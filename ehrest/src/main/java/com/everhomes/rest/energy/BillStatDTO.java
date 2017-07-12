package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 *     <li>serviceDayStats: 性质列表 {@link com.everhomes.rest.energy.ServiceStatDTO}</li>
 *     <li>billCategoryId: 项目id</li>
 *     <li>billCategoryName: 项目名称</li>
 *     <li>dayBillStats: 项目汇总list</li>
 * </ul>
 */
public class BillStatDTO {
 
    @ItemType(ServiceStatDTO.class)
    private List<ServiceStatDTO> serviceDayStats; 

    @ItemType(DayStatDTO.class)
    private List<DayStatDTO> dayBillStats;  
    
    private Long billCategoryId;
    private String billCategoryName;
 
    public List<ServiceStatDTO> getServiceDayStats() {
		return serviceDayStats;
	}

	public void setServiceDayStats(List<ServiceStatDTO> serviceDayStats) {
		this.serviceDayStats = serviceDayStats;
	}

	public Long getBillCategoryId() {
		return billCategoryId;
	}

	public void setBillCategoryId(Long billCategoryId) {
		this.billCategoryId = billCategoryId;
	}

	public String getBillCategoryName() {
		return billCategoryName;
	}

	public void setBillCategoryName(String billCategoryName) {
		this.billCategoryName = billCategoryName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<DayStatDTO> getDayBillStats() {
		return dayBillStats;
	}

	public void setDayBillStats(List<DayStatDTO> dayBillStats) {
		this.dayBillStats = dayBillStats;
	}
}
