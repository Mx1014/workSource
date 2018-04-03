package com.everhomes.rest.energy;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>dayStats: 每日读数 {@link com.everhomes.rest.energy.DayStatDTO}</li>
 *     <li>meterName: 表名</li>
 *     <li>meterNumber: 表号</li>
 *     <li>meterRate: 倍率</li>
 *      * </ul>
 */
public class MeterStatDTO {

    private Long meterId;
    private Long billCategoryId;
    private Long serviceCategoryId;
    private String meterName;
    private String meterNumber;
    private BigDecimal meterRate;
    private BigDecimal meterPrice;

    @ItemType(DayStatDTO.class)
    private List<DayStatDTO> dayStats;  
    
	public Long getBillCategoryId() {
		return billCategoryId;
	}

	public void setBillCategoryId(Long billCategoryId) {
		this.billCategoryId = billCategoryId;
	}

	public Long getServiceCategoryId() {
		return serviceCategoryId;
	}

	public void setServiceCategoryId(Long serviceCategoryId) {
		this.serviceCategoryId = serviceCategoryId;
	}

	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	public String getMeterNumber() {
		return meterNumber;
	}

	public void setMeterNumber(String meterNumber) {
		this.meterNumber = meterNumber;
	}

	public BigDecimal getMeterRate() {
		return meterRate;
	}

	public void setMeterRate(BigDecimal meterRate) {
		this.meterRate = meterRate;
	}

	public BigDecimal getMeterPrice() {
		return meterPrice;
	}

	public void setMeterPrice(BigDecimal meterPrice) {
		this.meterPrice = meterPrice;
	}

	public List<DayStatDTO> getDayStats() {
		return dayStats;
	}

	public void setDayStats(List<DayStatDTO> dayStats) {
		this.dayStats = dayStats;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}
}
