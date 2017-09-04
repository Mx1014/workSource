package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>status: status</li>
 *     <li>description: description</li>
 *     <li>flowMainId: flowMainId</li>
 *     <li>createTime: createTime</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>flowVersion: flowVersion</li>
 *     <li>nodeLevel: nodeLevel</li>
 *     <li>nodeName: nodeName</li>
 *     <li>params: params</li>
 *     <li>autoStepType: autoStepType</li>
 *     <li>allowApplierUpdate: allowApplierUpdate</li>
 *     <li>autoStepMinute: autoStepMinute</li>
 *     <li>allowTimeoutAction: allowTimeoutAction</li>
 *     <li>reminder: reminder {@link com.everhomes.rest.flow.FlowNodeReminderDTO}</li>
 *     <li>tracker: tracker {@link com.everhomes.rest.flow.FlowNodeTrackerDTO}</li>
 *     <li>flowLaneId: flowLaneId</li>
 *     <li>nodeType: nodeType</li>
 *     <li>needAllProcessorComplete: needAllProcessorComplete</li>
 *     <li>branch: branch {@link com.everhomes.rest.flow.FlowBranchDTO}</li>
 *     <li>processors: processors {@link com.everhomes.rest.flow.FlowUserSelectionDTO}</li>
 *     <li>processButtons: processButtons {@link com.everhomes.rest.flow.FlowButtonDetailDTO}</li>
 *     <li>conditions: conditions {@link com.everhomes.rest.flow.FlowConditionDTO}</li>
 * </ul>
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

    private FlowNodeReminderDTO reminder;
    private FlowNodeTrackerDTO tracker;

    private Long flowLaneId;
    private String nodeType;
    private Long needAllProcessorComplete;

    private FlowBranchDTO branch;

    @ItemType(FlowUserSelectionDTO.class)
    private List<FlowUserSelectionDTO> processors = new ArrayList<>();

    @ItemType(FlowButtonDetailDTO.class)
    private List<FlowButtonDetailDTO> processButtons = new ArrayList<>();

    @ItemType(FlowConditionDTO.class)
    private List<FlowConditionDTO> conditions = new ArrayList<>();

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

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public FlowBranchDTO getBranch() {
        return branch;
    }

    public void setBranch(FlowBranchDTO branch) {
        this.branch = branch;
    }

    public Long getFlowLaneId() {
        return flowLaneId;
    }

    public void setFlowLaneId(Long flowLaneId) {
        this.flowLaneId = flowLaneId;
    }

    public Long getNeedAllProcessorComplete() {
        return needAllProcessorComplete;
    }

    public void setNeedAllProcessorComplete(Long needAllProcessorComplete) {
        this.needAllProcessorComplete = needAllProcessorComplete;
    }

    public List<FlowConditionDTO> getConditions() {
        return conditions;
    }

    public void setConditions(List<FlowConditionDTO> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
