package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>applyUserId: 申请的用户ID</li>
 * <li>applyUserName: 申请用户名字</li>
 * <li>flowMainId: 工作流ID</li>
 * <li>moduleId: 业务模块ID</li>
 * <li>moduleName: 业务名字</li>
 * <li>moduleLink: 路由信息，比如 zl://停车月卡申请/[申请ID] </li>
 * <li>content: 业务处理的内容 </li>
 * <li>createTime: 创建时间 </li>
 * <li>status: @{link com.everhomes.rest.flow.FlowCaseStatus} </li>
 * <li>isEvaluate: 0: 不需要评价， 1:待评价，2: 已评价</li>
 * <li>evaluate_score: 评价分数</li>
 * <li>allowApplierUpdate: 是否可以编辑</li>
 * </ul>
 * @author janson
 *
 */
public class FlowCaseDTO {
    private Long     id;
    private Long     applyUserId;
    private String applyUserName;
    private Long     flowMainId;
    private String     ownerType;
    private Long     referId;
    private String     referType;
    private String     moduleType;
    private Long     processUserId;
    private String processUserName;
    private Integer     namespaceId;
    private String     content;
    private Byte     status;
    private Integer     flowVersion;
    private Long     createTime;
    private Long     moduleId;
    private String moduleName;
    private Long     currentNodeId;
    private Long     ownerId;
    private String moduleLink;
    private Byte isEvaluate;
    private Integer evaluateScore;
    private Long lastStepTime;
    private Byte allowApplierUpdate;
    
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

	public String getApplyUserName() {
		return applyUserName;
	}


	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
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

	public String getProcessUserName() {
		return processUserName;
	}


	public void setProcessUserName(String processUserName) {
		this.processUserName = processUserName;
	}


	public Long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}


	public Byte getIsEvaluate() {
		return isEvaluate;
	}


	public void setIsEvaluate(Byte isEvaluate) {
		this.isEvaluate = isEvaluate;
	}


	public Integer getEvaluateScore() {
		return evaluateScore;
	}


	public void setEvaluateScore(Integer evaluateScore) {
		this.evaluateScore = evaluateScore;
	}


	public Long getLastStepTime() {
		return lastStepTime;
	}


	public void setLastStepTime(Long lastStepTime) {
		this.lastStepTime = lastStepTime;
	}


	public Byte getAllowApplierUpdate() {
		return allowApplierUpdate;
	}


	public void setAllowApplierUpdate(Byte allowApplierUpdate) {
		this.allowApplierUpdate = allowApplierUpdate;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

