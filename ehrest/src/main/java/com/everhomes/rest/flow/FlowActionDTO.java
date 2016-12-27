package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowActionDTO {
	private Long     id;
    private Byte     status;
    private String     renderText;
    private Long     flowMainId;
    private String     stepType;
    private Integer     namespaceId;
    private String     actionType;
    private Timestamp     createTime;
    private Integer     flowVersion;
    private Long     belongTo;
    private String     belongEntity;
    private Long reminderAfterMinute;
    private Long reminderTickMinute;
    private Long trackerProcessor;
    private Long trackerApplier;
    private Long allowTimeoutAction;
    private Long templateId;
    private Byte enabled;
    
	@ItemType(FlowUserSelectionDTO.class)
	List<FlowUserSelectionDTO> processors;

    public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getRenderText() {
		return renderText;
	}

	public void setRenderText(String renderText) {
		this.renderText = renderText;
	}

	public Long getFlowMainId() {
		return flowMainId;
	}

	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getFlowVersion() {
		return flowVersion;
	}

	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(Long belongTo) {
		this.belongTo = belongTo;
	}

	public String getBelongEntity() {
		return belongEntity;
	}

	public void setBelongEntity(String belongEntity) {
		this.belongEntity = belongEntity;
	}

	public Long getReminderAfterMinute() {
		return reminderAfterMinute;
	}

	public void setReminderAfterMinute(Long reminderAfterMinute) {
		this.reminderAfterMinute = reminderAfterMinute;
	}

	public Long getReminderTickMinute() {
		return reminderTickMinute;
	}

	public void setReminderTickMinute(Long reminderTickMinute) {
		this.reminderTickMinute = reminderTickMinute;
	}

	public Long getTrackerProcessor() {
		return trackerProcessor;
	}

	public void setTrackerProcessor(Long trackerProcessor) {
		this.trackerProcessor = trackerProcessor;
	}

	public Long getTrackerApplier() {
		return trackerApplier;
	}

	public void setTrackerApplier(Long trackerApplier) {
		this.trackerApplier = trackerApplier;
	}

	public List<FlowUserSelectionDTO> getProcessors() {
		return processors;
	}

	public void setProcessors(List<FlowUserSelectionDTO> processors) {
		this.processors = processors;
	}

	public Long getAllowTimeoutAction() {
		return allowTimeoutAction;
	}

	public void setAllowTimeoutAction(Long allowTimeoutAction) {
		this.allowTimeoutAction = allowTimeoutAction;
	}

	public Byte getEnabled() {
		return enabled;
	}

	public void setEnabled(Byte enabled) {
		this.enabled = enabled;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

