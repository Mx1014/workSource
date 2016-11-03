package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页的锚点, 没有下页则无该值</li>
 *     <li>logs: 读表记录DTO列表 {@link com.everhomes.rest.energy.EnergyMeterReadingLogDTO}</li>
 * </ul>
 */
public class SearchEnergyMeterReadingLogsResponse {

    private Long nextPageAnchor;
    @ItemType(EnergyMeterReadingLogDTO.class)
    private List<EnergyMeterReadingLogDTO> logs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<EnergyMeterReadingLogDTO> getLogs() {
        return logs;
    }

    public void setLogs(List<EnergyMeterReadingLogDTO> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
