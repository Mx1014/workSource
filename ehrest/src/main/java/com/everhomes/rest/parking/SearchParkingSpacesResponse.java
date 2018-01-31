package com.everhomes.rest.parking;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>spaceDTOS: 车位列表，{@link com.everhomes.rest.parking.ParkingSpaceDTO}</li>
 * <li>nextPageAnchor: 下一页瞄</li>
 * </ul>
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
