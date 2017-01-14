package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowNodePriority {
    private Long     id;
    private Integer     nodeLevel;
    private String     nodeName;
    private String     description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getNodeLevel() {
		return nodeLevel;
	}

	public void setNodeLevel(Integer nodeLevel) {
		this.nodeLevel = nodeLevel;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
