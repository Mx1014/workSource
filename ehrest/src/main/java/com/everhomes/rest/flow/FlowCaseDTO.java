package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowCaseDTO {
    private Long     applyUserId;
    private Long     flowMainId;
    private String     ownerType;
    private Long     referId;
    private String     moduleType;
    private Long     id;
    private Long     processUserId;;
    private Timestamp     lastStepTime;
    private Integer     namespaceId;
    private String     content;
    private Long     rejectNodeId;
    private Integer     rejectCount;
    private Byte     status;
    private Integer     flowVersion;
    private Timestamp     createTime;
    private Integer     moduleId;
    private Long     currentNodeId;
    private String     referType;
    private Long     ownerId;
    private String moduleLink;


    public Long getApplyUserId() {
		return applyUserId;
	}


	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
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


	public Long getReferId() {
		return referId;
	}


	public void setReferId(Long referId) {
		this.referId = referId;
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


	public Long getProcessUserId() {
		return processUserId;
	}


	public void setProcessUserId(Long processUserId) {
		this.processUserId = processUserId;
	}


	public Timestamp getLastStepTime() {
		return lastStepTime;
	}


	public void setLastStepTime(Timestamp lastStepTime) {
		this.lastStepTime = lastStepTime;
	}


	public Integer getNamespaceId() {
		return namespaceId;
	}


	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Long getRejectNodeId() {
		return rejectNodeId;
	}


	public void setRejectNodeId(Long rejectNodeId) {
		this.rejectNodeId = rejectNodeId;
	}


	public Integer getRejectCount() {
		return rejectCount;
	}


	public void setRejectCount(Integer rejectCount) {
		this.rejectCount = rejectCount;
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


	public Timestamp getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}


	public Integer getModuleId() {
		return moduleId;
	}


	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}


	public Long getCurrentNodeId() {
		return currentNodeId;
	}


	public void setCurrentNodeId(Long currentNodeId) {
		this.currentNodeId = currentNodeId;
	}


	public String getReferType() {
		return referType;
	}


	public void setReferType(String referType) {
		this.referType = referType;
	}


	public Long getOwnerId() {
		return ownerId;
	}


	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}


	public String getModuleLink() {
		return moduleLink;
	}


	public void setModuleLink(String moduleLink) {
		this.moduleLink = moduleLink;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

