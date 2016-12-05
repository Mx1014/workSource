package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowEvaluateDTO {
    private Byte     star;
    private Long     userId;
    private Long     flowMainId;
    private Integer     flowVersion;
    private String     ownerType;
    private Integer     namespaceId;
    private Long     flowCaseId;
    private Long     ownerId;
    private String     moduleType;
    private Long     id;
    private Integer     moduleId;
    private Long flowNodeId;


    public Byte getStar() {
		return star;
	}


	public void setStar(Byte star) {
		this.star = star;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Long getFlowMainId() {
		return flowMainId;
	}


	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}


	public Integer getFlowVersion() {
		return flowVersion;
	}


	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}


	public String getOwnerType() {
		return ownerType;
	}


	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}


	public Integer getNamespaceId() {
		return namespaceId;
	}


	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


	public Long getFlowCaseId() {
		return flowCaseId;
	}


	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}


	public Long getOwnerId() {
		return ownerId;
	}


	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}


	public String getModuleType() {
		return moduleType;
	}


	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Integer getModuleId() {
		return moduleId;
	}


	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

