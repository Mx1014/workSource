package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListScriptsResponse {
	@ItemType(FlowScriptDTO.class)
	private List<FlowScriptDTO> scripts;

	public List<FlowScriptDTO> getScripts() {
		return scripts;
	}

	public void setScripts(List<FlowScriptDTO> scripts) {
		this.scripts = scripts;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
