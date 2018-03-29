package com.everhomes.rest.parking;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 * <li>nextPageAnchor: 下一页瞄</li>
 * <li>logDTOS: 车位操作日志列表，{@link com.everhomes.rest.parking.ParkingSpaceLogDTO}</li>
 * </ul>
 */
public class ListParkingSpaceLogsResponse {
    private Long nextPageAnchor;

    @ItemType(ParkingSpaceLogDTO.class)
    private List<ParkingSpaceLogDTO> logDTOS;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ParkingSpaceLogDTO> getLogDTOS() {
        return logDTOS;
    }

    public void setLogDTOS(List<ParkingSpaceLogDTO> logDTOS) {
        this.logDTOS = logDTOS;
    }
}
