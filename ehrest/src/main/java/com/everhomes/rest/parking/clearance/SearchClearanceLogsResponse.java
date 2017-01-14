// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页锚点</li>
 *     <li>logs: 申请记录列表 {@link com.everhomes.rest.parking.clearance.ParkingClearanceLogDTO}</li>
 * </ul>
 */
public class SearchClearanceLogsResponse {

    private Long nextPageAnchor;
    @ItemType(ParkingClearanceLogDTO.class)
    private List<ParkingClearanceLogDTO> logs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ParkingClearanceLogDTO> getLogs() {
        return logs;
    }

    public void setLogs(List<ParkingClearanceLogDTO> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
