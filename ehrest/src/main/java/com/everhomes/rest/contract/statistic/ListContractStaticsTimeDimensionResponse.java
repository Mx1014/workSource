package com.everhomes.rest.contract.statistic;

import java.util.List;

import com.everhomes.util.StringHelper;
/**
 *<ul>
 * 	<li>resultList：结果集</li>
 *	<li>nextPageAnchor</li>
 *</ul>
 */
public class ListContractStaticsTimeDimensionResponse {
	
	private List<TotalContractStaticsDTO> resultList;
	private Long nextPageAnchor;
	
	public List<TotalContractStaticsDTO> getResultList() {
		return resultList;
	}


	public void setResultList(List<TotalContractStaticsDTO> resultList) {
		this.resultList = resultList;
	}


	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}


	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
