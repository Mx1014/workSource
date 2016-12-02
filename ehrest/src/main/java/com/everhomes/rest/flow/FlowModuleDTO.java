package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowModuleDTO {
	private Long moduleId;
	private String moduleName;
	private String displayName;
	
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
