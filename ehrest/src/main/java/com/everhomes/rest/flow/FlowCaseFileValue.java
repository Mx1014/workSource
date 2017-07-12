package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class FlowCaseFileValue {
	@ItemType(FlowCaseFileDTO.class)
	private List<FlowCaseFileDTO> files;

	public List<FlowCaseFileDTO> getFiles() {
		return files;
	}

	public void setFiles(List<FlowCaseFileDTO> files) {
		this.files = files;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	
}
