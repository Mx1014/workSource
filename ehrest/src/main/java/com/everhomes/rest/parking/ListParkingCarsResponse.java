package com.everhomes.rest.parking;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/11/6.
 */
public class ListParkingCarsResponse {
    private Long nextPageAnchor;

    @ItemType(ParkingCarBriefDTO.class)
    private List<ParkingCarBriefDTO> requests;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ParkingCarBriefDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<ParkingCarBriefDTO> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
