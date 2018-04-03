package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>status: status</li>
 *     <li>flowMainId: flowMainId</li>
 *     <li>flowVersion: flowVersion</li>
 *     <li>description: description</li>
 *     <li>createTime: createTime</li>
 *     <li>nodeLevel: nodeLevel</li>
 *     <li>nodeName: nodeName</li>
 *     <li>allowTimeoutAction: allowTimeoutAction</li>
 *     <li>autoStepType: autoStepType</li>
 *     <li>allowApplierUpdate: allowApplierUpdate</li>
 *     <li>autoStepMinute: autoStepMinute</li>
 *     <li>params: params</li>
 *     <li>nodeType: 节点类型{@link com.everhomes.rest.flow.FlowNodeType}</li>
 *     <li>flowLaneId: 泳道id</li>
 *     <li>needAllProcessorComplete: 节点会签开关</li>
 *     <li>branch: 分支信息 {@link com.everhomes.rest.flow.FlowBranchDTO}</li>
 *     <li>processors: 处理人列表 {@link com.everhomes.rest.flow.FlowUserSelectionDTO}</li>
 * </ul>
 */
public class FlowNodeDTO {

    private Long id;
    private Integer namespaceId;
    private Byte status;
    private Long flowMainId;
    private Integer flowVersion;
    private String description;
    private Timestamp createTime;
    private Integer nodeLevel;
    private String nodeName;
    private Byte allowTimeoutAction;
    private String autoStepType;
    private Byte allowApplierUpdate;
    private Integer autoStepMinute;
    private String params;

    private String nodeType;
    private Long flowLaneId;
    private Long needAllProcessorComplete;

    private FlowBranchDTO branch;

    @ItemType(FlowUserSelectionDTO.class)
    private List<FlowUserSelectionDTO> processors = new ArrayList<>();

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

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
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

    public FlowBranchDTO getBranch() {
        return branch;
    }

    public void setBranch(FlowBranchDTO branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

