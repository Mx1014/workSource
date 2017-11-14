package com.everhomes.rest.parking;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/10/31.
 */
public class ListParkingCarVerificationsResponse {

    private Long nextPageAnchor;

    @ItemType(ParkingCarVerificationDTO.class)
    private List<ParkingCarVerificationDTO> requests;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ParkingCarVerificationDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<ParkingCarVerificationDTO> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
