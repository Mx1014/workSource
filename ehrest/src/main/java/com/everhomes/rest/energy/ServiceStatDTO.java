package com.everhomes.rest.energy;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 *     <li>meterDayStats: 表的列表 {@link com.everhomes.rest.energy.MeterStatDTO}</li>
 *     <li>serviceCategoryId: 性质id</li>
 *     <li>serviceCategoryName: 性质名称</li>
 *     <li>dayServiceStats: 性质汇总每日list</li>
 * </ul>
 */
public class ServiceStatDTO {

    @ItemType(MeterStatDTO.class)
    private List<MeterStatDTO> meterDayStats;  

    @ItemType(DayStatDTO.class)
    private List<DayStatDTO> dayServiceStats;  
    
    private Long serviceCategoryId;
    private String serviceCategoryName;
	public List<MeterStatDTO> getMeterDayStats() {
		return meterDayStats;
	}
	public void setMeterDayStats(List<MeterStatDTO> meterDayStats) {
		this.meterDayStats = meterDayStats;
	}
	public Long getServiceCategoryId() {
		return serviceCategoryId;
	}
	public void setServiceCategoryId(Long serviceCategoryId) {
		this.serviceCategoryId = serviceCategoryId;
	}
	public String getServiceCategoryName() {
		return serviceCategoryName;
	}
	public void setServiceCategoryName(String serviceCategoryName) {
		this.serviceCategoryName = serviceCategoryName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public List<DayStatDTO> getDayServiceStats() {
		return dayServiceStats;
	}
	public void setDayServiceStats(List<DayStatDTO> dayServiceStats) {
		this.dayServiceStats = dayServiceStats;
	}
}
