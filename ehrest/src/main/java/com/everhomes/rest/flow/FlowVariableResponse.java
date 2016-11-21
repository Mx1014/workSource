package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class FlowVariableResponse {
	@ItemType(FlowVariableDTO.class)
	private List<FlowVariableDTO> dtos;

    public List<FlowVariableDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<FlowVariableDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
