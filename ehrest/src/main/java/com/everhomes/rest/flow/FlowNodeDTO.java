package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowNodeDTO {
    private Byte     status;
    private String     description;
    private Long     flowMainId;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private Integer     flowVersion;
    private Integer     nodeLevel;
    private Long     id;
    private String     nodeName;
    private Byte allowTimeoutAction;
    private String autoStepType;
    private Byte allowApplierUpdate;
    private Integer autoStepMinute;
    private String params;
    
	@ItemType(FlowUserSelectionDTO.class)
	List<FlowUserSelectionDTO> processors;

    public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAutoStepType() {
		return autoStepType;
	}

	public void setAutoStepType(String autoStepType) {
		this.autoStepType = autoStepType;
	}

	public Byte getAllowApplierUpdate() {
		return allowApplierUpdate;
	}

	public void setAllowApplierUpdate(Byte allowApplierUpdate) {
		this.allowApplierUpdate = allowApplierUpdate;
	}

	public Integer getAutoStepMinute() {
		return autoStepMinute;
	}

	public void setAutoStepMinute(Integer autoStepMinute) {
		this.autoStepMinute = autoStepMinute;
	}

	public Long getFlowMainId() {
		return flowMainId;
	}

	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Integer getFlowVersion() {
		return flowVersion;
	}

	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}

	public Integer getNodeLevel() {
		return nodeLevel;
	}

	public void setNodeLevel(Integer nodeLevel) {
		this.nodeLevel = nodeLevel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Byte getAllowTimeoutAction() {
		return allowTimeoutAction;
	}

	public void setAllowTimeoutAction(Byte allowTimeoutAction) {
		this.allowTimeoutAction = allowTimeoutAction;
	}

	public List<FlowUserSelectionDTO> getProcessors() {
		return processors;
	}

	public void setProcessors(List<FlowUserSelectionDTO> processors) {
		this.processors = processors;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

