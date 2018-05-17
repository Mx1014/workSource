package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalStatisticsDTO;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */
public class QueryOrgRentalStatisticsResponse {
    private Long nextPageAnchor;
    @ItemType(RentalStatisticsDTO.class)
    private List<RentalStatisticsDTO> orgStatistics;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<RentalStatisticsDTO> getOrgStatistics() {
        return orgStatistics;
    }

    public void setOrgStatistics(List<RentalStatisticsDTO> orgStatistics) {
        this.orgStatistics = orgStatistics;
    }
}
