package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>rangePrice: 区间价钱，参考{@link com.everhomes.rest.energy.EnergyMeterRangePriceDTO}</li>
 * </ul>
 */
public class EnergyMeterPriceConfigExpressionDTO {

    @ItemType(EnergyMeterRangePriceDTO.class)
    private List<EnergyMeterRangePriceDTO> rangePrice;

    public List<EnergyMeterRangePriceDTO> getRangePrice() {
        return rangePrice;
    }

    public void setRangePrice(List<EnergyMeterRangePriceDTO> rangePrice) {
        this.rangePrice = rangePrice;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
