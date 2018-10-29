package com.everhomes.rest.yellowPage.faq;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListOperateServicesResponse {
	
	private Long nextPageAnchor;
	private List<OperateServiceDTO> dtos;
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<OperateServiceDTO> getDtos() {
		return dtos;
	}
	public void setDtos(List<OperateServiceDTO> dtos) {
		this.dtos = dtos;
	}
	
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
