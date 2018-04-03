package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页的锚点, 没有下页则无该值</li>
 *     <li>meters: 表记DTO列表 {@link com.everhomes.rest.energy.EnergyMeterDTO}</li>
 * </ul>
 */
public class SearchEnergyMeterResponse {

    private Long nextPageAnchor;
    @ItemType(EnergyMeterDTO.class)
    private List<EnergyMeterDTO> meters;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<EnergyMeterDTO> getMeters() {
        return meters;
    }

    public void setMeters(List<EnergyMeterDTO> meters) {
        this.meters = meters;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
