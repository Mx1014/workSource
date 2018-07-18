package com.everhomes.rest.contract;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor: 下页的锚点 </li>
 * <li>requests: 结果：小区列表{@link com.everhomes.rest.contract.ContractTemplateDTO}</li> 
 * </ul>
 * Created by jm.ding on 2018/6/27.
 */
public class ListContractTemplatesResponse {

	private Long nextPageAnchor;
	
	@ItemType(ContractTemplateDTO.class)
	private List<ContractTemplateDTO> requests;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ContractTemplateDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<ContractTemplateDTO> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
