package com.everhomes.rest.parking;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * @author sw on 2017/12/18.
 */
public class SearchParkingSpacesResponse {
    private Long nextPageAnchor;

    @ItemType(ParkingSpaceDTO.class)
    private List<ParkingSpaceDTO> spaceDTOS;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ParkingSpaceDTO> getSpaceDTOS() {
        return spaceDTOS;
    }

    public void setSpaceDTOS(List<ParkingSpaceDTO> spaceDTOS) {
        this.spaceDTOS = spaceDTOS;
    }
}
