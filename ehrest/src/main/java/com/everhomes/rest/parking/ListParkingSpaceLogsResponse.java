package com.everhomes.rest.parking;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/12/18.
 */
public class ListParkingSpaceLogsResponse {
    private Long nextPageAnchor;

    @ItemType(ParkingSpaceLogDTO.class)
    private List<ParkingSpaceLogDTO> logDTOS;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<ParkingSpaceLogDTO> getLogDTOS() {
        return logDTOS;
    }

    public void setLogDTOS(List<ParkingSpaceLogDTO> logDTOS) {
        this.logDTOS = logDTOS;
    }
}
