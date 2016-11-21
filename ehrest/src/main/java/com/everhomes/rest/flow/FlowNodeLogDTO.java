package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class FlowNodeLogDTO {
	private Long nodeId;
	private String nodeName;
	
	@ItemType(FlowEventLogDTO.class)
	private List<FlowEventLogDTO> logs;

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public List<FlowEventLogDTO> getLogs() {
		return logs;
	}

	public void setLogs(List<FlowEventLogDTO> logs) {
		this.logs = logs;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
