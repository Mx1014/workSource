package com.everhomes.flow;

import com.everhomes.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

public class FlowModuleInfo implements Comparable<FlowModuleInfo> {

    public static final String META_KEY_FORM_FLAG = "formSupportFlag";// meta里的表单标志, 对应的值为：1：有表单， 0：没有表单

	private Long moduleId;
	private String moduleName;
	private Integer priority;
	private Map<String, Object> meta;
	
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

	public Map<String, Object> getMeta() {
        if (this.meta == null) {
            this.meta = new HashMap<>();
        }
		return meta;
	}

    public void addMeta(String key, String value) {
        if (this.meta == null) {
            this.meta = new HashMap<>();
        }
        this.meta.put(key, value);
    }

    public Object getMeta(String key) {
        if (this.meta == null) {
			return null;
        }
        return this.meta.get(key);
    }

	public void setMeta(Map<String, Object> meta) {
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
