// @formatter:off
package com.everhomes.rest.contract;

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
public class ListContractsResponse {

	private Long nextPageAnchor;
	private Long totalNum;

	@ItemType(ContractDTO.class)
	private List<ContractDTO> contracts;

	public ListContractsResponse() {

	}

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

	public ListContractsResponse(Long nextPageAnchor, List<ContractDTO> contracts) {
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

	public List<ContractDTO> getContracts() {
		return contracts;
	}

	public void setContracts(List<ContractDTO> contracts) {
		this.contracts = contracts;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
