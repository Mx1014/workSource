package com.everhomes.rest.flow;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class FlowGraphDetailDTO {
	private Long     id;
    private String     flowName;
    private Long     flowMainId;
    private String     ownerType;
    private String     moduleType;
    private Integer     namespaceId;
    private Byte     status;
    private Integer     flowVersion;
    private Timestamp     runTime;
    private Timestamp     createTime;
    private Long     moduleId;
    private Long     ownerId;
    private Long projectId;
    private String projectType;
    
    @ItemType(FlowNodeDetailDTO.class)
    List<FlowNodeDetailDTO> nodes;
    
	@ItemType(FlowUserSelectionDTO.class)
	List<FlowUserSelectionDTO> supervisors;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public Long getFlowMainId() {
		return flowMainId;
	}

	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getFlowVersion() {
		return flowVersion;
	}

	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}

	public Timestamp getRunTime() {
		return runTime;
	}

	public void setRunTime(Timestamp runTime) {
		this.runTime = runTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public List<FlowNodeDetailDTO> getNodes() {
		return nodes;
	}

	public void setNodes(List<FlowNodeDetailDTO> nodes) {
		this.nodes = nodes;
	}

	public List<FlowUserSelectionDTO> getSupervisors() {
		return supervisors;
	}

	public void setSupervisors(List<FlowUserSelectionDTO> supervisors) {
		this.supervisors = supervisors;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
