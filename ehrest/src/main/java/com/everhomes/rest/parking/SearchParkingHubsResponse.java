package com.everhomes.rest.parking;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>hubList: hub列表，{@link ParkingHubDTO}</li>
 * <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 */
public class SearchParkingHubsResponse {
    private Long nextPageAnchor;

    @ItemType(ParkingHubDTO.class)
    private List<ParkingHubDTO> hubList;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ParkingHubDTO> getHubList() {
        return hubList;
    }

    public void setHubList(List<ParkingHubDTO> hubList) {
        this.hubList = hubList;
    }
}
