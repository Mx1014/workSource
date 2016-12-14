package com.everhomes.flow;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

public class FlowCaseDetail {
	private Long id;
	private Integer namespaceId;
	private Long ownerId;
	private String ownerType;
	private Long moduleId;
	private String moduleType;
	private String moduleName;
	private String applierName;
	private String applierPhone;
	private Long flowMainId;
	private Integer flowVersion;
	private Long applyUserId;
	private Long processUserId;
	private Long referId;
	private String referType;
	private Long currentNodeId;
	private Byte status;
	private Integer rejectCount;
	private Long rejectNodeId;
	private Long stepCount;
	private Timestamp lastStepTime;
	private Timestamp createTime;
	private String caseType;
	private String content;
	private Integer evaluateScore;
	private String stringTag1;
	private String stringTag2;
	private String stringTag3;
	private String stringTag4;
	private String stringTag5;
	private Long integralTag1;
	private Long integralTag2;
	private Long integralTag3;
	private Long integralTag4;
	private Long integralTag5;
	
	private Long eventLogId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getApplierName() {
		return applierName;
	}
	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}
	public String getApplierPhone() {
		return applierPhone;
	}
	public void setApplierPhone(String applierPhone) {
		this.applierPhone = applierPhone;
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
	public Long getApplyUserId() {
		return applyUserId;
	}
	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}
	public Long getProcessUserId() {
		return processUserId;
	}
	public void setProcessUserId(Long processUserId) {
		this.processUserId = processUserId;
	}
	public Long getReferId() {
		return referId;
	}
	public void setReferId(Long referId) {
		this.referId = referId;
	}
	public String getReferType() {
		return referType;
	}
	public void setReferType(String referType) {
		this.referType = referType;
	}
	public Long getCurrentNodeId() {
		return currentNodeId;
	}
	public void setCurrentNodeId(Long currentNodeId) {
		this.currentNodeId = currentNodeId;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Integer getRejectCount() {
		return rejectCount;
	}
	public void setRejectCount(Integer rejectCount) {
		this.rejectCount = rejectCount;
	}
	public Long getRejectNodeId() {
		return rejectNodeId;
	}
	public void setRejectNodeId(Long rejectNodeId) {
		this.rejectNodeId = rejectNodeId;
	}
	public Long getStepCount() {
		return stepCount;
	}
	public void setStepCount(Long stepCount) {
		this.stepCount = stepCount;
	}
	public Timestamp getLastStepTime() {
		return lastStepTime;
	}
	public void setLastStepTime(Timestamp lastStepTime) {
		this.lastStepTime = lastStepTime;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getEvaluateScore() {
		return evaluateScore;
	}
	public void setEvaluateScore(Integer evaluateScore) {
		this.evaluateScore = evaluateScore;
	}
	public String getStringTag1() {
		return stringTag1;
	}
	public void setStringTag1(String stringTag1) {
		this.stringTag1 = stringTag1;
	}
	public String getStringTag2() {
		return stringTag2;
	}
	public void setStringTag2(String stringTag2) {
		this.stringTag2 = stringTag2;
	}
	public String getStringTag3() {
		return stringTag3;
	}
	public void setStringTag3(String stringTag3) {
		this.stringTag3 = stringTag3;
	}
	public String getStringTag4() {
		return stringTag4;
	}
	public void setStringTag4(String stringTag4) {
		this.stringTag4 = stringTag4;
	}
	public String getStringTag5() {
		return stringTag5;
	}
	public void setStringTag5(String stringTag5) {
		this.stringTag5 = stringTag5;
	}
	public Long getIntegralTag1() {
		return integralTag1;
	}
	public void setIntegralTag1(Long integralTag1) {
		this.integralTag1 = integralTag1;
	}
	public Long getIntegralTag2() {
		return integralTag2;
	}
	public void setIntegralTag2(Long integralTag2) {
		this.integralTag2 = integralTag2;
	}
	public Long getIntegralTag3() {
		return integralTag3;
	}
	public void setIntegralTag3(Long integralTag3) {
		this.integralTag3 = integralTag3;
	}
	public Long getIntegralTag4() {
		return integralTag4;
	}
	public void setIntegralTag4(Long integralTag4) {
		this.integralTag4 = integralTag4;
	}
	public Long getIntegralTag5() {
		return integralTag5;
	}
	public void setIntegralTag5(Long integralTag5) {
		this.integralTag5 = integralTag5;
	}

	public Long getEventLogId() {
		return eventLogId;
	}
	public void setEventLogId(Long eventLogId) {
		this.eventLogId = eventLogId;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
