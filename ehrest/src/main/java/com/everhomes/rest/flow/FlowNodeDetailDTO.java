package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 节点的详细信息，包括名字，包括消息提醒，包括任务跟踪，其它信息
 * @author janson
 *
 */
public class FlowNodeDetailDTO {
    private Long id;
    private Byte status;
    private String description;
    private Long flowMainId;
    private Timestamp createTime;
    private Integer namespaceId;
    private Integer flowVersion;
    private Integer nodeLevel;
    private String nodeName;
    private String params;
    private String autoStepType;
    private Byte allowApplierUpdate;
    private Integer autoStepMinute;
    private Byte allowTimeoutAction;

    @ItemType(FlowUserSelectionDTO.class)
    private
    List<FlowUserSelectionDTO> processors = new ArrayList<>();

    private FlowNodeReminderDTO reminder;

    private FlowNodeTrackerDTO tracker;

    @ItemType(FlowButtonDetailDTO.class)
    private
    List<FlowButtonDetailDTO> processButtons = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getStatus() {
        return status;
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

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public List<FlowUserSelectionDTO> getProcessors() {
        return processors;
    }

    public void setProcessors(List<FlowUserSelectionDTO> processors) {
        this.processors = processors;
    }

    public FlowNodeReminderDTO getReminder() {
        return reminder;
    }

    public void setReminder(FlowNodeReminderDTO reminder) {
        this.reminder = reminder;
    }

    public FlowNodeTrackerDTO getTracker() {
        return tracker;
    }

    public void setTracker(FlowNodeTrackerDTO tracker) {
        this.tracker = tracker;
    }

    public List<FlowButtonDetailDTO> getProcessButtons() {
        return processButtons;
    }

    public void setProcessButtons(List<FlowButtonDetailDTO> processButtons) {
        this.processButtons = processButtons;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
