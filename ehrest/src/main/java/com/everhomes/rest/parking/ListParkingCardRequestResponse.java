package com.everhomes.rest.parking;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>requests: 月卡申请信息列表，参考{@link com.everhomes.rest.parking.ParkingCardRequestDTO}</li>
 * </ul>
 */
public class ListParkingCardRequestResponse {
    private Long nextPageAnchor;

    @ItemType(ParkingCardRequestDTO.class)
    private List<ParkingCardRequestDTO> requests;
    private Long totalNum;
    
    
    public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

	public ListParkingCardRequestResponse() {
    }
    
    public ListParkingCardRequestResponse(Long nextPageAnchor, List<ParkingCardRequestDTO> requests) {
        this.nextPageAnchor = nextPageAnchor;
        this.requests = requests;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ParkingCardRequestDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<ParkingCardRequestDTO> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
