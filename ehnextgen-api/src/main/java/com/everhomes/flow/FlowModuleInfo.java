package com.everhomes.flow;

import java.util.Map;

import com.everhomes.util.StringHelper;

public class FlowModuleInfo implements Comparable<FlowModuleInfo> {
	private Long moduleId;
	private String moduleName;
	private Integer priority;
	private Map<String, String> meta;
	
	public FlowModuleInfo() {
		priority = 0;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

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

	public Map<String, String> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, String> meta) {
		this.meta = meta;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	@Override
	public int compareTo(FlowModuleInfo o) {
		return this.getPriority().compareTo(o.getPriority());
	}
}
