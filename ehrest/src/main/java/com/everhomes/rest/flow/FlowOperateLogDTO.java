package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>flowCaseId: flowCaseId</li>
 *     <li>flowCaseTitle: flowCaseTitle</li>
 *     <li>flowCaseContent: flowCaseContent</li>
 *     <li>flowNodeId: flowNodeId</li>
 *     <li>flowNodeName: flowNodeName</li>
 *     <li>flowButtonId: flowButtonId</li>
 *     <li>flowButtonName: flowButtonName</li>
 *     <li>stepType: stepType</li>
 * </ul>
 */
public class FlowOperateLogDTO {

    private Long id;
    private Long flowCaseId;
    private String flowCaseTitle;
    private String flowCaseContent;
    private Long flowNodeId;
    private String flowNodeName;
    private Long flowButtonId;
    private String flowButtonName;
    private String stepType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public String getFlowCaseTitle() {
        return flowCaseTitle;
    }

    public void setFlowCaseTitle(String flowCaseTitle) {
        this.flowCaseTitle = flowCaseTitle;
    }

    public String getFlowCaseContent() {
        return flowCaseContent;
    }

    public void setFlowCaseContent(String flowCaseContent) {
        this.flowCaseContent = flowCaseContent;
    }

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public String getFlowNodeName() {
        return flowNodeName;
    }

    public void setFlowNodeName(String flowNodeName) {
        this.flowNodeName = flowNodeName;
    }

    public Long getFlowButtonId() {
        return flowButtonId;
    }

    public void setFlowButtonId(Long flowButtonId) {
        this.flowButtonId = flowButtonId;
    }

    public String getFlowButtonName() {
        return flowButtonName;
    }

    public void setFlowButtonName(String flowButtonName) {
        this.flowButtonName = flowButtonName;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

