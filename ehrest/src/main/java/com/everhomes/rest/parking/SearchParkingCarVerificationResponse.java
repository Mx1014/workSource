package com.everhomes.rest.parking;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: nextPageAnchor</li>
 * <li>requests: requests {@link com.everhomes.rest.parking.ParkingCarVerificationDTO}</li>
 * </ul>
 */
public class SearchParkingCarVerificationResponse {

    private Long nextPageAnchor;

    @ItemType(ParkingCarVerificationDTO.class)
    private List<ParkingCarVerificationDTO> requests;
    private Long totalNum;

    
    public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

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
