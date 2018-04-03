package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListFlowModulesResponse {
	@ItemType(FlowModuleDTO.class)
	private List<FlowModuleDTO> modules;

	public List<FlowModuleDTO> getModules() {
		return modules;
	}

	public void setModules(List<FlowModuleDTO> modules) {
		this.modules = modules;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
