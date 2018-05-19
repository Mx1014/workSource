// @formatter:off
package com.everhomes.parkingtest;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>contracts: 合同列表，参考{@link com.everhomes.rest.contract.ContractDTO}</li>
 * </ul>
 */
public class ParkingLotTestResponse {

	private Long nextPageAnchor;

	@ItemType(ParkingLotTestDTO.class)
	private List<ParkingLotTestDTO> contracts;

	public ParkingLotTestResponse() {

	}

	public ParkingLotTestResponse(Long nextPageAnchor, List<ParkingLotTestDTO> contracts) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.contracts = contracts;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ParkingLotTestDTO> getContracts() {
		return contracts;
	}

	public void setContracts(List<ParkingLotTestDTO> contracts) {
		this.contracts = contracts;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
