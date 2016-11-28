package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul> 工作流的详细信息
 * <li>entities: 实体的详细信息 {@link com.everhomes.rest.flow.FlowCaseEntity}</li>
 * </ul>
 * @author janson
 *
 */
public class FlowCaseDetailDTO {
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
    
    @ItemType(FlowCaseEntity.class)
    private List<FlowCaseEntity> entities;
    
	@ItemType(FlowNodeLogDTO.class)
	private List<FlowNodeLogDTO> nodes;
	
	@ItemType(FlowButtonDTO.class)
	private List<FlowButtonDTO> buttons;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
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

	public String getReferType() {
		return referType;
	}

	public void setReferType(String referType) {
		this.referType = referType;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public Long getProcessUserId() {
		return processUserId;
	}

	public void setProcessUserId(Long processUserId) {
		this.processUserId = processUserId;
	}

	public String getProcessUserName() {
		return processUserName;
	}

	public void setProcessUserName(String processUserName) {
		this.processUserName = processUserName;
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

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
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

	public List<FlowButtonDTO> getButtons() {
		return buttons;
	}

	public void setButtons(List<FlowButtonDTO> buttons) {
		this.buttons = buttons;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
