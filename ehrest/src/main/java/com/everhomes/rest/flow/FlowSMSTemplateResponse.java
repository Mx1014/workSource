package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class FlowSMSTemplateResponse {
	@ItemType(FlowSMSTemplateDTO.class)
	private List<FlowSMSTemplateDTO> dtos;
	
	private Long nextPageAnchor;

	public List<FlowSMSTemplateDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<FlowSMSTemplateDTO> dtos) {
		this.dtos = dtos;
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
